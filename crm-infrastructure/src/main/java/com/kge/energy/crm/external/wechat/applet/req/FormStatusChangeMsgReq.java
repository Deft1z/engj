package com.kge.energy.crm.external.wechat.applet.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 工单状态变更通知
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/sendMessage.html
 */
@Data
@Accessors(chain = true)
public class FormStatusChangeMsgReq {

    @JsonProperty("thing4")
    @Schema(description = "服务单位")
    private Value serviceUnit;

    @JsonProperty("thing5")
    @Schema(description = "服务人")
    private Value servicePerson;

    @JsonProperty("phrase3")
    @Schema(description = "状态")
    private Value status;

    @JsonProperty("time6")
    @Schema(description = "处理时间")
    private Value handleTime;

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    public static class Value {
        private String value;
    }

}
