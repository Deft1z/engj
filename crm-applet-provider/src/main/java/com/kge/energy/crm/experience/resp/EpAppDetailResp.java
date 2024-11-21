package com.kge.energy.crm.experience.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "APP详情响应参数", description = "APP详情响应参数")
public class EpAppDetailResp {

    @Schema(description = "应用ID")
    private Integer appId;

    @Schema(description = "应用名称")
    private String name;

    @Schema(description = "应用地址")
    private String appAddress;

    @Schema(description = "绑定地址")
    private String bindAddress;

    @Schema(description = "是否常用")
    private Boolean commonlyUsed;

    @Schema(description = "体验数据附件")
    private List<ExperienceAttachment> experienceAttachments;

    @Data
    @Accessors(chain = true)
    public static class ExperienceAttachment {

        @Schema(description = "模拟数据类型")
        private String type;

        @Schema(description = "附件地址")
        private String filepath;
    }
}
