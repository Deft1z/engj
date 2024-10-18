package com.kge.energy.crm.dashboard.resp;

import com.kge.energy.crm.repository.entityext.result.OrganizationParameter;
import lombok.Data;

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
     * 集团总部；电力建设；工程咨询设计；科技服务与智慧能源；物业运营；城建建设；
     */
    private String type;

    /**
     * 总共两层
     */
    private Integer level;

    /**
     * 为集团以及旗下16家子公司名称
     */
    private String name;

    private OrganizationParameter parameter;

}
