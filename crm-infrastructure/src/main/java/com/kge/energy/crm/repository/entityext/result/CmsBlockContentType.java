package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;

@Data
public class CmsBlockContentType {

    /**
     * 模块内容
     */
    private Integer blockContentId;

    /**
     * cms_block主键
     */
    private Integer blockId;

    private String openId;

    /**
     * b_app主键,可为空
     */
    private Integer appId;

    private String appUuid;

    private String appAddress;

    private Integer bindType;

    private String bindAddress;

    private String scope;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String desc;

    private String type;

    /**
     * 图片链接
     */
    private String imageUrl;

    private Integer fileId;

    private String pageUrl;

    private Integer pagesId;

    /**
     * H5介绍页面链接
     */
    private Integer pageFile;

    private String pageName;

    private String remark;
}
