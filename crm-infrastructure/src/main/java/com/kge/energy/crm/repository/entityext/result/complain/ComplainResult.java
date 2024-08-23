package com.kge.energy.crm.repository.entityext.result.complain;

import lombok.Data;

@Data
public class ComplainResult {

    /**
     * 投诉记录id
     */
    private Integer complainId;

    private Integer formId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 1 问题；2 投诉
     */
    private Integer typef;

    /**
     * 进度慢；质量差；态度恶劣
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 投诉公司
     */
    private String company;

    /**
     * 合同内容
     */
    private String contacts;

    /**
     * 手机
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 0 待处理；1 正在处理；2 完成
     */
    private Integer status;

    /**
     * 反馈内容
     */
    private String feedback;

    /**
     * 处理用户ID
     */
    private Integer processUserId;

    /**
     * 处理时间
     */
    private String processTime;

    /**
     * 标记
     */
    private Integer flag;

    /**
     * 创建用户ID
     */
    private Integer createUserId;

    /**
     * 修改用户ID
     */
    //private Integer modifyUserId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 服务公司
     */
    private String serviceCompany;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 订单编码
     */
    private String orderCode;

    /**
     * 合同编码
     */
    //private String contractCode;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 合同状态
     */
    //private String contractStatus;

    /**
     * 文件路径
     */
    private String filepath;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 真实姓名
     */
    private String realname;

}
