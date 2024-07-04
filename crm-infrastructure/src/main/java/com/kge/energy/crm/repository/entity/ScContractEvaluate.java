package com.kge.energy.crm.repository.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * sc_contract_evaluate 服务合同评价(ScContractEvaluate)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
public class ScContractEvaluate {

    /**
     * 合同评价表
     */
    @TableId(type = IdType.AUTO)
    private Integer contractEvaluateId; 

    /**
     * 合同ID（wf_form主键）
     */
    private Integer serviceContractId; 

    /**
     * 满意度：1-5
     */
    private Integer satisfaction; 

    /**
     * 评价
     */
    private String evaluate; 
}

