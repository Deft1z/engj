package com.kge.energy.crm.resource.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class ResourceBean {

    /**
     * 资源ID
     */
    private Integer resourceId;

    /**
     * 父资源ID
     */
    private Integer parentResourceId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 资源类型：menu、button
     */
    private String resourceType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 资源地址
     */
    private String path;

    /**
     * 资源地址类型：system、file、app、link
     */
    private String pathType;

    /**
     * 图标编码
     */
    private String iconCode;

    /**
     * 图标文件路径
     */
    private String iconFilePath;

    /**
     * 菜单状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 系统类型：applet、mgr
     */
    private String systemType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 子资源
     */
    private List<ResourceBean> childrens;
}
