package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 用户(BUser)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class BUser {

    @TableId(type = IdType.AUTO)
    private Integer userId; 

    /**
     * 内部名称不显示
     */
    private String name; 

    /**
     * 经过加盐加密后保存的密码
     */
    private String passwd; 

    /**
     * 盐值
     */
    private String passwdSalt; 

    /**
     * 用户类型
     */
    private String type; 

    /**
     * 小程序显示名称
     */
    private String realname; 

    /**
     * 手机
     */
    private String mobile; 

    /**
     * 微信小程序openid
     */
    private String openId; 

    /**
     * 公司
     */
    private String company; 

    /**
     * 地址
     */
    private String address; 

    /**
     * 软删除标识
     */
    private Integer flag; 

    /**
     * 创建用户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId; 

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime; 

    /**
     * 修改用户ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId; 

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 

    /**
     * 备注
     */
    private String remark; 
}

