package com.kge.energy.crm.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.kge.energy.crm.iam.service.IamSyncLogService;
import com.kge.energy.crm.iam.service.IamUserService;
import com.kge.energy.crm.repository.entity.IamSyncLog;
import com.kge.energy.crm.repository.entity.IamUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 零信任办公系统数据（组织、用户）同步任务
 *
 * @author zhengwenke
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class IamDataSyncTask {

    public static final String IAM_RESP_CODE_KEY = "resultCode";

    public static final String IAM_RESP_DATA_KEY = "body";

    public static final String IAM_RESP_DATA_NEXT_PAGE_KEY = "cookie";

    public static final String IAM_RESP_DATA_USERS_KEY = "users";

    public static final String IAM_RESP_CODE_SUCCESS_VAL = "SIM-00000";

    public static final String TIME_ZONE = "GMT%2B8";

    @Value("${iam.base-url:http://172.18.54.76:8080/sim}")
    private String baseUrl;

    @Value("${iam.app-user:AUTH_CRM}")
    private String appUser;

    @Value("${iam.private-key:8wlhpq2v754lro5kcg8lmqw17fid6uj6}")
    private String privateKey;

    @Value("${iam.sync.default-page-size:200}")
    private String defaultPageSize;

    @Value("${iam.sync.log-keep-days:5}")
    private Integer logKeepDays;

    private final IamUserService iamUserService;

    private final IamSyncLogService iamSyncLogService;

    /**
     * 定时删除历史同步日志
     */
    @Scheduled(cron = "${iam.sync.log-clean-cron:0 15 0 * * ?}")
    public void deleteHisLogTask() {
        log.info("==> 执行iam历史同步日志删除任务");
        LocalDateTime logKeepDate = LocalDateTime.now().minusDays(logKeepDays);
        int effectedRows = iamSyncLogService.deleteHisLogs(logKeepDate);
        log.info("<== 执行iam历史同步日志删除任务完成，已删除[{}]前的[{}]条历史同步日志", logKeepDate, effectedRows);
    }

    /**
     * 同步零信任办公系统数据
     * 头部鉴权参数 appuser、randomcode、timestamp、encodekey、sign
     * appuser: IDM授权账号(AUTH_CRM)
     * privatekey: IDM授权密钥(8wlhpq2v754lro5kcg8lmqw17fid6uj6)
     * randomcode: 随机字符串（字母和数字）
     * timestamp: 时间戳(yyyyMMddHHmmss'Z')，以Z结尾(默认不能比SIM服务器快/慢300秒)
     * encodekey: DigestUtils.sha256Hex(StringUtils.join(appuser, randomStr, dateNow, "{", appkey, "}"));
     * sign: 数字签名，DigestUtils.md5Hex(StringUtils.join(uri, "&", body, "&", appkey));
     */
    @Scheduled(cron = "${iam.sync.exec-cron:0 */5 * * * ?}")
    public synchronized void syncData() {
        log.debug("==> 执行iam数据同步任务...");
        syncUser(iamUserService.getLatestModifyTime(), "");
        log.debug("<== 执行iam数据同步任务完成");
    }

    private void syncUser(String latestModifyTime, String pageCookie) {
        String syncName = String.format("iam用户数据同步[%s]", pageCookie);
        String syncContent = "";
        String syncResult = "";
        Boolean successFlag = false;
        try {
            logSyncStartMsg(syncName);

            String filter = URLEncoder.encode("userNormalModifyTimestamp>=" + latestModifyTime, StandardCharsets.UTF_8);
            Map<String, Object> response = getIamRequest(filter, defaultPageSize, pageCookie, TIME_ZONE);

            String code = response.get(IAM_RESP_CODE_KEY).toString();
            if (code.equals(IAM_RESP_CODE_SUCCESS_VAL)) {
                Object data = response.get(IAM_RESP_DATA_KEY);
                syncContent = JSONUtil.toJsonStr(data);
                String nextPage = BeanUtil.beanToMap(data).get(IAM_RESP_DATA_NEXT_PAGE_KEY).toString();
                String users = JSONUtil.toJsonStr(BeanUtil.beanToMap(data).get(IAM_RESP_DATA_USERS_KEY));

                List<IamUser> iamUsers = JSONUtil.toList(users, IamUser.class);
                int addRows = 0;
                int updRows = 0;
                int hadRows = 0;
                for (IamUser iamUser : iamUsers) {
                    IamUser user = iamUserService.getById(iamUser.getSimId());
                    if (user == null) {
                        iamUserService.insert(iamUser);
                        addRows++;
                    } else if (!iamUserService.checkHadSync(iamUser.getSimId(), iamUser.getUserNormalModifyTimestamp())) {
                        iamUserService.update(iamUser);
                        updRows++;
                    } else {
                        hadRows++;
                    }
                }
                syncResult = formatSyncResult(addRows, updRows, hadRows);
                successFlag = true;
                logSuccessMsg(syncName, syncResult);
                if (StringUtils.isNotBlank(nextPage)) {
                    syncUser(latestModifyTime, nextPage);
                }
            } else {
                syncResult = JSONUtil.toJsonStr(response);
                logSyncFailMsg(syncName, syncResult);
            }
        } catch (Exception e) {
            syncResult = e.getMessage();
            logSyncExMsg(syncName, syncResult);
        } finally {
            addLog(syncName, syncContent, syncResult, successFlag);
        }
    }

    private Map<String, Object> getIamRequest(String filter, String pageSize, String cookie, String timeZone) {
        final String randomCode = RandomUtil.randomString(16).toLowerCase();
        final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN)) + "Z";
        final String encodeKey = DigestUtils.sha256Hex(StringUtils.join(appUser, randomCode, timestamp, "{", privateKey, "}"));

        final String uriFormat = "/api/v3/user/paged?filter=(%s)&pageSize=%s&cookie=%s&timeZone=%s";
        String uri = String.format(uriFormat, filter, pageSize, cookie, timeZone);
        final String sign = DigestUtils.md5Hex(StringUtils.join(uri, "&", null, "&", privateKey));

        String curl = "curl -H 'Content-Type: application/json' -H 'appuser: %s' -H 'randomcode: %s' -H 'timestamp: %s' -H 'encodekey: %s' -H 'sign: %s' \"http://172.18.54.76:8080/sim%s\"";
        curl = String.format(curl, appUser, randomCode, timestamp, encodeKey, sign, uri);
        log.info("==> curl command: {}", curl);

        String resp = HttpRequest.get(baseUrl + uri)
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("appuser", appUser)
                .header("randomcode", randomCode)
                .header("timestamp", timestamp)
                .header("encodekey", encodeKey)
                .header("sign", sign)
                .execute().body();

        return JSONUtil.toBean(resp, Map.class);
    }

    private void addLog(String syncName, String syncContent, String syncResult, Boolean successFlag) {
        iamSyncLogService.insert(IamSyncLog.builder()
                .syncTime(LocalDateTime.now())
                .syncName(syncName)
                .syncContent(syncContent)
                .syncResult(syncResult)
                .successFlag(successFlag)
                .build()
        );
    }

    private void logSyncStartMsg(String syncName) {
        log.info("==> 执行{}任务", syncName);
    }

    private String formatSyncResult(int addRows, int updRows, int hadRows) {
        return String.format("新增[%s]条记录，更新[%s]条记录，已同步[%s]条记录。", addRows, updRows, hadRows);
    }

    private void logSuccessMsg(String syncName, String syncResult) {
        log.info("<== 执行{}任务完成，{}", syncName, syncResult);
    }

    private void logSyncFailMsg(String syncName, String syncResult) {
        log.info("<== 执行{}任务失败: {}", syncName, syncResult);
    }

    private void logSyncExMsg(String syncName, String exMsg) {
        log.error("<== 执行{}任务异常: {}", syncName, exMsg);
    }

}