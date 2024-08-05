package com.kge.energy.crm.resource.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class ResourceBean {

    @Schema(description = "资源ID")
    private Integer resourceId;

    @Schema(description = "父资源ID")
    private Integer parentResourceId;

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "资源编码")
    private String resourceCode;

    @Schema(description = "资源类型：menu、button")
    private String resourceType;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "资源地址")
    private String path;

    @Schema(description = "资源地址类型：system、file、app、link")
    private String pathType;

    @Schema(description = "图标编码")
    private String iconCode;

    @Schema(description = "图标文件路径")
    private String iconFilePath;

    @Schema(description = "菜单状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "系统类型：applet、mgr")
    private String systemType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户id")
    private Integer tenantId;

    @Schema(description = "子资源")
    private List<ResourceBean> childrens;
}
