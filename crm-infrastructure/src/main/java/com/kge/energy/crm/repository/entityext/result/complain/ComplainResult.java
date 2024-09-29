package com.kge.energy.crm.repository.entityext.result.complain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ComplainResult {

    /**
     * 投诉记录id
     */
    @ExcelIgnore
    private Integer complainId;

    /*
     *  工单id
     */
    @ExcelIgnore
    private Integer formId;

    /**
     * 用户ID
     */
    @ExcelIgnore
    private Integer userId;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private String createTime;

    /**
     * 投诉类型：1 工单投诉；2 合同投诉
     */
    @ExcelProperty("投诉类型")
    private Integer typef;

    /**
     * 投诉主题：进度慢；质量差；态度恶劣
     */
    @ExcelProperty("投诉主题")
    private String subject;

    /**
     * 投诉内容
     */
    @ExcelProperty("投诉内容")
    private String content;

    /**
     * 投诉公司
     */
    @ExcelIgnore
    private String company;

    /**
     * 合同内容
     */
    @ExcelIgnore
    private String contacts;

    /**
     * 手机
     */
    @ExcelIgnore
    private String phone;

    /**
     * 地址
     */
    @ExcelIgnore
    private String address;

    /**
     * 服务公司
     */
    @ExcelProperty("服务公司")
    private String serviceCompany;

    /**
     * 业务名称
     */
    @ExcelProperty("业务名称")
    private String businessName;

    /**
     * 0 待处理；1 正在处理；2 完成
     */
//    @ExcelProperty(value = "处理状态",converter = StatusConverter.class)
    @ExcelProperty(value = "处理状态")
    private Integer status;

    /**
     * 反馈内容
     */
    @ExcelIgnore
    private String feedback;

    /**
     * 处理用户ID
     */
    @ExcelIgnore
    private Integer processUserId;

    /**
     * 处理时间
     */
    @ExcelIgnore
    private String processTime;

    /**
     * 标记
     */
    @ExcelIgnore
    private Integer flag;

    /**
     * 创建用户ID
     */
    @ExcelIgnore
    private Integer createUserId;

    /*
      修改用户ID
     */
    //private Integer modifyUserId;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 合同名称
     */
    @ExcelIgnore
    private String contractName;


    /**
     * 订单编码
     */
    @ExcelIgnore
    private String orderCode;

    /**
     * 合同编码
     */
    @ExcelIgnore
    private String contractCode;

    /**
     * 订单状态
     */
    @ExcelIgnore
    private String orderStatus;

    /**
     * 合同状态
     */
    @ExcelIgnore
    private String contractStatus;

    /**
     * 文件路径
     */
    @ExcelIgnore
    private String filepath;

    /**
     * 组织名称
     */
    @ExcelIgnore
    private String orgName;

    /**
     * 真实姓名
     */
    @ExcelIgnore
    private String realname;

}
