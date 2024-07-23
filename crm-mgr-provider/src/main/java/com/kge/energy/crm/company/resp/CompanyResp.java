package com.kge.energy.crm.company.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyResp {
    /**
     * 集团及二级公司组织表
     */
    private Integer organizationId;

    /**
     * 上一级组织ID
     */
    private Integer parentOrganizationId;

    /**
     * 停用
     */
    private Integer regionId;

    /**
     * 停用
     */
    private Integer userTenantId;

    /**
     * 总共两层
     */
    private Integer level;

    /**
     * 停用
     */
    private String authCode;

    /**
     * 为集团以及旗下16家子公司名称
     */
    private String name;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 经度
     */
    private String lng;

    /**
     * 停用
     */
    private String variables;

    /**
     * 停用
     */
    private String referSource;

    /**
     * 停用
     */
    private String referId;

    /**
     * 软删除标识
     */
    private Integer flag;

    /**
     * 创建用户ID
     */
    private Integer createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改用户ID
     */
    private Integer modifyUserId;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    private String remark;

    // TODO 上面多数字段冗余，以后需要去除
    private String filepath;

    @JsonProperty("type")
    private String tag;

    private String fullName;

    private Integer serviceType;

    private String serviceTypeString;

    @JsonProperty("typeList")
    private String[] tagList;
}
