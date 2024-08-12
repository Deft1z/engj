package com.kge.energy.crm.resource.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "新增菜单资源请求参数", description = "新增菜单资源请求参数")
public class AddResourceReq {

    @Schema(description = "父资源ID")
    private Integer parentResourceId;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String resourceName;

    @Schema(description = "菜单编码")
    private String resourceCode;

    @Schema(description = "菜单类型：menu、button", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Pattern(regexp = "menu|button")
    private String resourceType;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "资源地址")
    private String path;

    @Schema(description = "资源地址类型：system、file、bapp、link")
    @Pattern(regexp = "system|file|bapp|liink")
    private String pathType;

    @Schema(description = "图标编码")
    private String iconCode;

    @Schema(description = "图标文件路径")
    private String iconFilePath;

    @Schema(description = "菜单状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "系统类型：applet、mgr", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Pattern(regexp = "applet|mgr")
    private String systemType;

    @Schema(description = "备注")
    private String remark;


}
