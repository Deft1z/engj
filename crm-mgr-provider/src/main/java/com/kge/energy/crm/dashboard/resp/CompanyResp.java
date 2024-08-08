package com.kge.energy.crm.dashboard.resp;

import com.kge.energy.crm.repository.entityext.result.OrganizationParameter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
// TODO 这里后续要和前端讨论需要的字段，不建议将organization表的所有字段返回
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
     * 集团总部；电力建设；工程咨询设计；科技服务与智慧能源；物业运营；城建建设；
     */
    private String type;

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

    private OrganizationParameter parameter;

    private String variables;

    private String referSource;

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

    /**
     * 备注
     */
    private String remark;
}
