package com.kge.energy.crm.external.epcpv.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


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
    private final static String PVM_INSTCAP_KEY_SUFFIX = "pvm_instcap";
    private final static String PVM_STAGE_KEY_SUFFIX = "pvm_stage";

    @Value("${spring.data.redis.front}")
    private String redisFront;

    public Map<String, Object> getAllPvInfo(EpcpvInfoReq pvInfoReq){
        Map<String, Object> resultMap = new HashMap<>();
        List<PvRegionResp> regions = new ArrayList<>();

        Integer proNum = 0;

        //region
        Map<String, Integer> zoneMap = new HashMap<>();
        for(int k = 0; k< zones.size(); k++){
            regions.add(new PvRegionResp(zones.get(k), "0", "0", "0", "0"));
            zoneMap.put(zones.get(k), regions.size() - 1);
        }


        Long regionRatio = stringRedisTemplate.opsForHash().size(redisFront + PVM_REGIONPRO_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd());
        if(regionRatio == 0){

            String url = epcpvProperties.getUrl() + epcpvProperties.getRegionstat() + "?queryDateEnd=" + pvInfoReq.getQueryDateEnd() + "&queryDateStart=" + pvInfoReq.getQueryDateStart();

            HttpHeaders headers = RestUtils.defaultJsonHeaders();
            headers.add("Authorization", genAccessToken());

            String resultStr = RestUtils.getForString(url, headers, null);
            JSONObject jsonObject = new JSONObject(resultStr, false, false);
            JSONArray regionStatItems = jsonObject.getJSONObject("data").getJSONArray("regionStatItems");
            for(int k = 0; k < regionStatItems.size(); k++){
                JSONObject item = regionStatItems.getJSONObject(k);
                regions.get(zoneMap.get(item.getStr("regionName"))).setProNum(String.valueOf(item.getInt("statVal")));
                proNum += item.getInt("statVal");
            }

            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONPRO_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName(), pvRegionResp.getProNum());
                Integer per = Integer.valueOf(pvRegionResp.getProNum());
                if(per != 0){
                    pvRegionResp.setProjectPer(String.format("%.2f", 100.0 * per/proNum));
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONPROPER_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName(), pvRegionResp.getProjectPer());
                } else {
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONPROPER_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName(), "0");
                }
            }

            stringRedisTemplate.expire(redisFront + PVM_REGIONPRO_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), Duration.ofMillis(PVM_KEY_EXPIRES_IN));
            stringRedisTemplate.expire(redisFront + PVM_REGIONPROPER_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), Duration.ofMillis(PVM_KEY_EXPIRES_IN));

        } else {
            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                pvRegionResp.setProNum(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONPRO_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName())));
                pvRegionResp.setProjectPer(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONPROPER_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName())));
                proNum += Integer.parseInt(Opt.ofBlankAble(pvRegionResp.getProNum()).orElse("0"));
            }
        }

        zoneMap.clear();
        for(int k = 0; k < regions.size(); k++) {
            zoneMap.put(regions.get(k).getName(), k);
        }

        Long regionCap = stringRedisTemplate.opsForHash().size(redisFront + PVM_REGIONCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd());
        Float capacity = 0.0f;
        if(regionCap == 0) {

            String url = epcpvProperties.getUrl() + epcpvProperties.getCapacitystat() + "?queryDateEnd=" + pvInfoReq.getQueryDateEnd() + "&queryDateStart=" + pvInfoReq.getQueryDateStart();
            HttpHeaders headers = RestUtils.defaultJsonHeaders();
            headers.add("Authorization", genAccessToken());

            String resultStr = RestUtils.getForString(url, headers, null);
            JSONObject jsonObject = new JSONObject(resultStr, false, false);
            JSONArray regionStatItems = jsonObject.getJSONObject("data").getJSONArray("regionStatItems");
            for(int k = 0; k < regionStatItems.size(); k++){
                JSONObject item = regionStatItems.getJSONObject(k);
                regions.get(zoneMap.get(item.getStr("regionName"))).setCapacity(String.valueOf(item.getInt("statVal")));
                capacity += item.getFloat("statVal");
            }

            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName(), pvRegionResp.getCapacity());
                Float per = Float.valueOf(pvRegionResp.getCapacity());
                if(per != 0){
                    pvRegionResp.setCapacityPer(String.format("%.2f", 100.0 * per/capacity));
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONCAPPER_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName(), pvRegionResp.getCapacityPer());
                } else {
                    stringRedisTemplate.opsForHash().put(redisFront + PVM_REGIONCAPPER_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName(), "0");
                }
            }

            stringRedisTemplate.expire(redisFront + PVM_REGIONCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), Duration.ofMillis(PVM_KEY_EXPIRES_IN));
            stringRedisTemplate.expire(redisFront + PVM_REGIONCAPPER_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), Duration.ofMillis(PVM_KEY_EXPIRES_IN));

        } else {
            for(int k = 0; k < regions.size(); k++){
                PvRegionResp pvRegionResp = regions.get(k);
                pvRegionResp.setProNum(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName())));
                pvRegionResp.setProjectPer(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_REGIONCAPPER_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), pvRegionResp.getName())));
                capacity += Float.parseFloat(Opt.ofBlankAble(pvRegionResp.getProNum()).orElse("0"));
            }
        }

        //trans
        String stageRatio = stringRedisTemplate.opsForValue().get(redisFront + PVM_STAGE_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd());
        if(StrUtil.isBlank(stageRatio)){
            String url = epcpvProperties.getUrl() + epcpvProperties.getStagestat() + "?queryDateEnd=" + pvInfoReq.getQueryDateEnd() + "&queryDateStart=" + pvInfoReq.getQueryDateStart();
            HttpHeaders headers = RestUtils.defaultJsonHeaders();
            headers.add("Authorization", genAccessToken());
            String resultStr = RestUtils.getForString(url, headers, null);
            stringRedisTemplate.opsForValue().set(redisFront + PVM_STAGE_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), resultStr, Duration.ofMillis(PVM_KEY_EXPIRES_IN*2));
        }
        getStage(stageRatio, resultMap);

        //capacity
        Long caps = stringRedisTemplate.opsForHash().size(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd());
        List<PvCapacityItemResp> capitems = new ArrayList<>();
        PvCapacityResp pvCapacityResp = new PvCapacityResp("0", "0", "0", "0", "0", capitems);
        resultMap.put("capacity", pvCapacityResp);
        if(caps == 0) {
            String url = epcpvProperties.getUrl() + epcpvProperties.getInststat() + "?queryDateEnd=" + pvInfoReq.getQueryDateEnd() + "&queryDateStart=" + pvInfoReq.getQueryDateStart();
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

            JSONArray otherItems = jsonObject.getJSONObject("data").getJSONArray("otherItems");
            for(int k = 0; k < otherItems.size(); k++){
                JSONObject item = otherItems.getJSONObject(k);
                PvCapacityItemResp pvCapacityItemResp = new PvCapacityItemResp(
                        item.getStr("itemName"),
                        String.format("%.2f", item.getFloat("itemVal")),
                        String.format("%.2f", item.getFloat("itemPct"))
                );
                capitems.add(pvCapacityItemResp);
            }

            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "total", pvCapacityResp.getTotal());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "own", pvCapacityResp.getOwn());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "other", pvCapacityResp.getOther());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "ownper", pvCapacityResp.getOwnper());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "otherper", pvCapacityResp.getOtherper());
            stringRedisTemplate.opsForHash().put(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "items", JSONUtil.toJsonStr(capitems));
            stringRedisTemplate.expire(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), Duration.ofMillis(PVM_KEY_EXPIRES_IN));

        } else {
            pvCapacityResp.setTotal(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "total")));
            pvCapacityResp.setOwn(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "own")));
            pvCapacityResp.setOther(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "other")));
            pvCapacityResp.setOwnper(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "ownper")));
            pvCapacityResp.setOtherper(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "otherper")));
            pvCapacityResp.setItems(JSONUtil.toList(String.valueOf(stringRedisTemplate.opsForHash().get(redisFront + PVM_INSTCAP_KEY_SUFFIX + pvInfoReq.getQueryDateStart() + pvInfoReq.getQueryDateEnd(), "items")), PvCapacityItemResp.class));
        }

        List<PvRegionResp> newregions = regions.stream()
                .sorted(Comparator.comparing((PvRegionResp p) -> Integer.parseInt(p.getProNum())).reversed())
                .collect(Collectors.toList());
        resultMap.put("regions", newregions);
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

    private void getStage(String stageRatio, Map<String, Object> resultMap){
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
}
