package com.kge.energy.crm.external.epcpv.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kge.energy.crm.external.epcpv.property.EpcpvProperties;
import com.kge.energy.crm.external.epcpv.req.EpcpvDetailsCondition;
import com.kge.energy.crm.external.epcpv.req.EpcpvDetailsReq;
import com.kge.energy.crm.external.epcpv.req.EpcpvInfoReq;
import com.kge.energy.crm.external.epcpv.resp.*;
import com.kge.platform.framework.web.util.RestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EpcpvService {

    private final EpcpvProperties epcpvProperties;
    private final StringRedisTemplate stringRedisTemplate;

    private final static List<String> zones = new ArrayList<>(Arrays.asList("海珠区", "天河区", "越秀区", "白云区", "番禺区", "黄埔区", "荔湾区", "南沙区", "增城区", "花都区", "从化区"));
    private final static String PVM_ACCESS_TOKEN_KEY_SUFFIX = "pvm_accesstoken";
    private final static String PVM_ACCESS_TOKEN_SUFFIX = "Bearer ";
    private final static Long PVM_ACCESS_TOKEN_EXPIRES_IN = 2 * 3600 * 1000L;
    private final static Long PVM_KEY_EXPIRES_IN = 5 * 60 *1000L;
    private final static String PVM_REGIONPRO_KEY_SUFFIX = "pvm_regionpro";
    private final static String PVM_REGIONPROPER_KEY_SUFFIX = "pvm_regionproper";
    private final static String PVM_REGIONCAP_KEY_SUFFIX = "pvm_regioncap";
    private final static String PVM_REGIONCAPPER_KEY_SUFFIX = "pvm_regioncapper";
    private final static String PVM_REGIONOWNCAP_KEY_SUFFIX = "pvm_regionowncap";
    private final static String PVM_REGIONOWNCAPPER_KEY_SUFFIX = "pvm_regionowncapper";
    private final static String PVM_INSTCAP_KEY_SUFFIX = "pvm_instcap";
    private final static String PVM_STAGE_KEY_SUFFIX = "pvm_stage";
    private final static String PVM_ALLCAP_KEY_SUFFIX = "pvm_allcap";

    @Value("${spring.data.redis.front}")
    private String redisFront;

    public Map<String, Object> getAllPvInfo(EpcpvInfoReq pvInfoReq){
        Map<String, Object> resultMap = new HashMap<>();
        List<PvRegionResp> regions = new ArrayList<>();

        Integer proNum = 0;

        //capacitoverall
        getCapacityTotal(pvInfoReq, resultMap);

        //region
        Map<String, Integer> zoneMap = new HashMap<>();
        for(int k = 0; k< zones.size(); k++){
            regions.add(new PvRegionResp(zones.get(k), "0", "0", "0", "0", "0", "0"));
            zoneMap.put(zones.get(k), regions.size() - 1);
        }


        Long regionRatio = stringRedisTemplate.opsForHash().size(redisFront + PVM_REGIONPRO_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""));
        if(regionRatio == 0){

            String url = epcpvProperties.getUrl() + epcpvProperties.getRegionstat() + "?queryDateEnd=" + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse("") + "&queryDateStart=" + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("");

            HttpHeaders headers = RestUtils.defaultJsonHeaders();
            headers.add("Authorization", genAccessToken());

            String resultStr = RestUtils.getForString(url, headers, null);
            JSONObject jsonObject = new JSONObject(resultStr, false, false);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray regionStatItems = data.getJSONArray("regionStatItems");
            for(int k = 0; k < regionStatItems.size(); k++){
                JSONObject item = regionStatItems.getJSONObject(k);
                regions.get(zoneMap.get(item.getStr("regionName"))).setProjectnum(String.valueOf(item.getInt("statVal")));
                proNum += item.getInt("statVal");
            }

            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONPRO_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), pvRegionResp.getProjectnum());
                Integer per = Integer.valueOf(pvRegionResp.getProjectnum());
                if(per != 0){
                    pvRegionResp.setProjectper(String.format("%.2f", 100.0 * per/proNum));
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONPROPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), pvRegionResp.getProjectper());
                } else {
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONPROPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), "0");
                }
            }

            stringRedisTemplate.expire(redisFront + PVM_REGIONPRO_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), Duration.ofMillis(PVM_KEY_EXPIRES_IN));
            stringRedisTemplate.expire(redisFront + PVM_REGIONPROPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), Duration.ofMillis(PVM_KEY_EXPIRES_IN));

        } else {
            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                pvRegionResp.setProjectnum(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONPRO_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName())));
                pvRegionResp.setProjectper(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONPROPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName())));
                proNum += Integer.parseInt(Opt.ofBlankAble(pvRegionResp.getProjectnum()).orElse("0"));
            }
        }

        zoneMap.clear();
        for(int k = 0; k < regions.size(); k++) {
            zoneMap.put(regions.get(k).getName(), k);
        }

        Long regionCap = stringRedisTemplate.opsForHash().size(redisFront + PVM_REGIONCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""));
        Float capacity = 0.0f;
        if(regionCap == 0) {

            //0 在建容量 - 预估容量
            String url = epcpvProperties.getUrl() + epcpvProperties.getCapacitystat() + "?queryDateEnd=" + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse("") + "&queryDateStart=" + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + "&queryType=0";
            HttpHeaders headers = RestUtils.defaultJsonHeaders();
            headers.add("Authorization", genAccessToken());

            String resultStr = RestUtils.getForString(url, headers, null);
            JSONObject jsonObject = new JSONObject(resultStr, false, false);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray regionStatItems = data.getJSONArray("regionStatItems");
            for(int k = 0; k < regionStatItems.size(); k++){
                JSONObject item = regionStatItems.getJSONObject(k);
                regions.get(zoneMap.get(item.getStr("regionName"))).setCapacity(String.valueOf(item.getInt("statVal")));
                capacity += item.getFloat("statVal");
            }

            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), pvRegionResp.getCapacity());
                Float per = Float.valueOf(pvRegionResp.getCapacity());
                if(per != 0){
                    pvRegionResp.setCapacityper(String.format("%.2f", 100.0 * per/capacity));
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONCAPPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), pvRegionResp.getCapacityper());
                } else {
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONCAPPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), "0");
                }
            }

            //1-并网容量
            capacity = 0.0f;
            String url1 = epcpvProperties.getUrl() + epcpvProperties.getCapacitystat() + "?queryDateEnd=" + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse("") + "&queryDateStart=" + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + "&queryType=1";
            HttpHeaders headers1 = RestUtils.defaultJsonHeaders();
            headers1.add("Authorization", genAccessToken());

            String resultStr1 = RestUtils.getForString(url1, headers1, null);
            JSONObject jsonObject1 = new JSONObject(resultStr1, false, false);
            JSONObject data1 = jsonObject1.getJSONObject("data");
            JSONArray regionStatItems1 = data1.getJSONArray("regionStatItems");
            for(int k = 0; k < regionStatItems1.size(); k++){
                JSONObject item = regionStatItems1.getJSONObject(k);
                regions.get(zoneMap.get(item.getStr("regionName"))).setOwncapacity(String.valueOf(item.getInt("statVal")));
                capacity += item.getFloat("statVal");
            }

            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONOWNCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), pvRegionResp.getOwncapacity());
                Float per = Float.valueOf(pvRegionResp.getOwncapacity());
                if(per != 0){
                    pvRegionResp.setOwncapacityper(String.format("%.2f", 100.0 * per/capacity));
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONOWNCAPPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), pvRegionResp.getOwncapacityper());
                } else {
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONOWNCAPPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName(), "0");
                }
            }

            stringRedisTemplate.expire(redisFront + PVM_REGIONCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), Duration.ofMillis(PVM_KEY_EXPIRES_IN));
            stringRedisTemplate.expire(redisFront + PVM_REGIONCAPPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), Duration.ofMillis(PVM_KEY_EXPIRES_IN));
            stringRedisTemplate.expire(redisFront + PVM_REGIONOWNCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), Duration.ofMillis(PVM_KEY_EXPIRES_IN));
            stringRedisTemplate.expire(redisFront + PVM_REGIONOWNCAPPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), Duration.ofMillis(PVM_KEY_EXPIRES_IN));

        } else {
            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                pvRegionResp.setCapacity(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName())));
                pvRegionResp.setCapacityper(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONCAPPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName())));
                pvRegionResp.setOwncapacity(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONOWNCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName())));
                pvRegionResp.setOwncapacityper(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONOWNCAPPER_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), pvRegionResp.getName())));
                capacity += Float.parseFloat(Opt.ofBlankAble(pvRegionResp.getProjectnum()).orElse("0"));
            }
        }

        //trans
        getStage(pvInfoReq, resultMap);

        //capacity
        Long caps = stringRedisTemplate.opsForHash().size(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""));
        List<PvCapacityItemResp> capitems = new ArrayList<>();
        PvCapacityResp pvCapacityResp = new PvCapacityResp("0", "0", "0", "0", "0", capitems);
        resultMap.put("capacity", pvCapacityResp);
        if(caps == 0) {
            String url = epcpvProperties.getUrl() + epcpvProperties.getInststat() + "?queryDateEnd=" + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse("") + "&queryDateStart=" + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("");
            HttpHeaders headers = RestUtils.defaultJsonHeaders();
            headers.add("Authorization", genAccessToken());

            String resultStr = RestUtils.getForString(url, headers, null);
            JSONObject jsonObject = new JSONObject(resultStr, false, false);
            float total = jsonObject.getJSONObject("data").getFloat("totalVal");
            float owner = jsonObject.getJSONObject("data").getFloat("ownerVal");
            float other = jsonObject.getJSONObject("data").getFloat("otherSumVal");
            pvCapacityResp.setTotal(String.format("%.2f", total));
            pvCapacityResp.setOwn(String.format("%.2f", owner));
            pvCapacityResp.setOther(String.format("%.2f", other));

            if(total != 0){
                pvCapacityResp.setOwnper(String.format("%.2f", 100 * owner / total));
                pvCapacityResp.setOtherper(String.format("%.2f", 100 * other / total));
            }

            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray otherItems = data.getJSONArray("otherItems");
            for(int k = 0; k < otherItems.size(); k++){
                JSONObject item = otherItems.getJSONObject(k);
                PvCapacityItemResp pvCapacityItemResp = new PvCapacityItemResp(
                        item.getStr("itemName"),
                        String.format("%.2f", item.getFloat("itemVal")),
                        String.format("%.2f", item.getFloat("itemPct"))
                );
                capitems.add(pvCapacityItemResp);
            }

            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "total", pvCapacityResp.getTotal());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "own", pvCapacityResp.getOwn());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "other", pvCapacityResp.getOther());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "ownper", pvCapacityResp.getOwnper());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "otherper", pvCapacityResp.getOtherper());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "items", JSONUtil.toJsonStr(capitems));
            stringRedisTemplate.expire(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), Duration.ofMillis(PVM_KEY_EXPIRES_IN));

        } else {
            pvCapacityResp.setTotal(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "total")));
            pvCapacityResp.setOwn(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "own")));
            pvCapacityResp.setOther(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "other")));
            pvCapacityResp.setOwnper(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "ownper")));
            pvCapacityResp.setOtherper(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "otherper")));
            pvCapacityResp.setItems(JSONUtil.toList(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), "items")), PvCapacityItemResp.class));
        }

        List<PvRegionResp> newregions = regions.stream()
                .sorted(Comparator.comparing((PvRegionResp p) -> Integer.parseInt(p.getProjectnum())).reversed())
                .collect(Collectors.toList());
        resultMap.put("region", newregions);
        resultMap.put("projectnum", String.valueOf(proNum));

        return resultMap;
    }

    public Map<String, Object> getProjectDetailsList(EpcpvDetailsReq req) {
        Map<String, Object> resultMap = new HashMap<>();
        List<EpcpvDetailsResp> list = new ArrayList<>();
        Integer total = 0;

        String url = epcpvProperties.getUrl() + epcpvProperties.getProlist();
        HttpHeaders headers = RestUtils.defaultJsonHeaders();
        headers.add("Authorization", genAccessToken());
        String resultStr = RestUtils.postForObject(url, headers, JSONUtil.toJsonStr(req), String.class);
        JSONObject data = JSONUtil.parseObj(resultStr).getJSONObject("data");
        total = data.getInt("total");

        JSONArray details = data.getJSONArray("list");
        for(Object object : details){
            JSONObject item = (JSONObject) object;
            EpcpvDetailsResp epcpvDetailsResp = new EpcpvDetailsResp(
                    item.getStr("projectName"),
                    item.getStr("regionName"),
                    item.getStr("stageShowName"),
                    item.getStr("projectMaster"),
                    String.format("%.2f", Opt.ofNullable(item.getFloat("capacity")).orElse(0.00f)),
                    item.getStr("startDate")
            );
            list.add(epcpvDetailsResp);
        }

        resultMap.put("list", list);
        resultMap.put("total", total);
        return resultMap;
    }

    public String genAccessToken(){
        String tokenString = stringRedisTemplate.opsForValue().get(redisFront + PVM_ACCESS_TOKEN_KEY_SUFFIX);
        if(StrUtil.isNotBlank(tokenString)){
            return tokenString;
        }

        Algorithm algorithm = Algorithm.HMAC256(epcpvProperties.getAppSecret());
        long timestamp = System.currentTimeMillis();
        Date issuedAt = new Date(timestamp);
        Date expiresAt = new Date(timestamp + PVM_ACCESS_TOKEN_EXPIRES_IN);
        tokenString = JWT.create()
                .withSubject(epcpvProperties.getAppId())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
        tokenString = PVM_ACCESS_TOKEN_SUFFIX + tokenString;

        stringRedisTemplate.opsForValue().set(redisFront + PVM_ACCESS_TOKEN_KEY_SUFFIX, tokenString, Duration.ofMillis(PVM_ACCESS_TOKEN_EXPIRES_IN));

        return tokenString;
    }

    private void getStage(EpcpvInfoReq pvInfoReq, Map<String, Object> resultMap){
        String stageRatio = stringRedisTemplate.opsForValue().get(redisFront + PVM_STAGE_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""));
        if(StrUtil.isBlank(stageRatio)){
            String url = epcpvProperties.getUrl() + epcpvProperties.getStagestat() + "?queryDateEnd=" + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse("") + "&queryDateStart=" + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("");
            HttpHeaders headers = RestUtils.defaultJsonHeaders();
            headers.add("Authorization", genAccessToken());
            String transResultStr = RestUtils.getForString(url, headers, null);
            stringRedisTemplate.opsForValue().set(redisFront + PVM_STAGE_KEY_SUFFIX + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("") + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse(""), transResultStr, Duration.ofMillis(PVM_KEY_EXPIRES_IN*2));
            stageRatio = transResultStr;
        }

        List<PvTranResp> trans = new ArrayList<>();
        JSONObject data = JSONUtil.parseObj(stageRatio).getJSONObject("data");
        JSONArray stageStatItems = data.getJSONArray("stageStatItems");
        for (int k = 0; k < stageStatItems.size(); k++){
            JSONObject item = stageStatItems.getJSONObject(k);

            PvTranResp pvTranResp = new PvTranResp();
            pvTranResp.setName(item.getStr("stageShowName"));
            pvTranResp.setCount(String.valueOf(item.getInt("statVal")));
            pvTranResp.setPer("0.00");
            if(k > 0){
                Integer lastCount = Integer.valueOf(trans.get(k-1).getCount());
                if(lastCount == 0){
                    trans.get(k-1).setPer("0.00");
                } else {
                    trans.get(k-1).setPer(String.format("%.2f", 100.0 * item.getInt("statVal") / lastCount));
                }
            }

            List<PvTranResp> tmpDetails = new ArrayList<>();
            JSONArray subStageStatItems = item.getJSONArray("mergeStageStatItems");
            if(CollUtil.isNotEmpty(subStageStatItems)){
                for(int v = 0; v < subStageStatItems.size(); v++){
                    JSONObject subItem = subStageStatItems.getJSONObject(v);

                    PvTranResp subPvTranResp = new PvTranResp();
                    subPvTranResp.setName(subItem.getStr("stageShowName"));
                    subPvTranResp.setCount(String.valueOf(subItem.getInt("statVal")));
                    subPvTranResp.setPer("0.00");
                    if(v > 0){
                        Integer subLastCount = Integer.valueOf(tmpDetails.get(v-1).getCount());
                        if(subLastCount == 0){
                            tmpDetails.get(v-1).setPer("0.00");
                        } else {
                            tmpDetails.get(v-1).setPer(String.format("%.2f", 100.0 * subItem.getInt("statVal") / subLastCount));
                        }
                    }
                    tmpDetails.add(subPvTranResp);
                }
            }
            pvTranResp.setDetail(tmpDetails);
            trans.add(pvTranResp);
        }

        resultMap.put("trans", trans);

        PvProjectStatResp pvProjectStatResp = new PvProjectStatResp();
        Integer normalCnt = data.getInt("normalCnt");
        pvProjectStatResp.setNormalproject(String.valueOf(normalCnt));

        Integer suspendCnt = data.getInt("suspendCnt");
        pvProjectStatResp.setSuspendproject(String.valueOf(suspendCnt));

        Integer allCnt = normalCnt + suspendCnt;
        if(allCnt == 0){
            pvProjectStatResp.setNormalprojectper("0.00");
            pvProjectStatResp.setSuspendprojectper("0.00");
        } else {
            pvProjectStatResp.setNormalprojectper(String.format("%.2f", 100.0 * normalCnt / allCnt));
            pvProjectStatResp.setSuspendprojectper(String.format("%.2f", 100.0 * suspendCnt / allCnt));
        }

        resultMap.put("projectstat", pvProjectStatResp);

    }

    private void getCapacityTotal(EpcpvInfoReq pvInfoReq, Map<String, Object> resultMap){
        PvRecentCapacity pvRecentCapacity = new PvRecentCapacity().setWeek("0").setMonth("0").setYear("0");
        try{
            String pvRecentCapacityStr = stringRedisTemplate.opsForValue().get(redisFront + PVM_ALLCAP_KEY_SUFFIX);
            if(StrUtil.isBlank(pvRecentCapacityStr)){
                String url = epcpvProperties.getUrl() + epcpvProperties.getCapacitytotal() + "?queryDateEnd=" + Opt.ofBlankAble(pvInfoReq.getQueryDateEnd()).orElse("") + "&queryDateStart=" + Opt.ofBlankAble(pvInfoReq.getQueryDateStart()).orElse("");
                HttpHeaders headers = RestUtils.defaultJsonHeaders();
                headers.add("Authorization", genAccessToken());
                String resultStr = RestUtils.getForString(url, headers, null);
                JSONObject result = JSONUtil.parseObj(resultStr);
                if(NumberUtil.equals(result.getInt("code"), Integer.valueOf(1))){
                    JSONObject data = result.getJSONObject("data");
                    JSONArray extendStatItems = data.getJSONArray("extendStatItems");
                    if(CollUtil.size(extendStatItems) == 3){
                        pvRecentCapacity.setWeek(String.format("%.2f", JSONUtil.parseObj(extendStatItems.get(0)).getFloat("itemVal")));
                        pvRecentCapacity.setMonth(String.format("%.2f", JSONUtil.parseObj(extendStatItems.get(1)).getFloat("itemVal")));
                        pvRecentCapacity.setYear(String.format("%.2f", JSONUtil.parseObj(extendStatItems.get(2)).getFloat("itemVal")));
                        pvRecentCapacityStr = JSONUtil.toJsonStr(pvRecentCapacity);
                        stringRedisTemplate.opsForValue().set(redisFront + PVM_ALLCAP_KEY_SUFFIX, pvRecentCapacityStr, Duration.ofMillis(PVM_KEY_EXPIRES_IN));
                    }
                }

            }
            pvRecentCapacity = JSONUtil.toBean(pvRecentCapacityStr, PvRecentCapacity.class, false);
        } catch (Exception e) {
            log.error("getCapacityTotal error: ",e);
        }

        resultMap.put("capacitoverall", pvRecentCapacity);
    }

    private void getRegion(List<PvRegionResp> regions, Map<String, Integer> zoneMap){

    }
}
