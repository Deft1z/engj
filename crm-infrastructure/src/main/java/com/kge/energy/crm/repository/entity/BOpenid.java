package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 用户对外编码(BOpenid)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class BOpenid {

    @TableId(type = IdType.AUTO)
    private Integer openidId; 

    /**
     * 对应b_app主键
     */
    private Integer appId; 

    /**
     * 对应b_user主键
     */
    private Integer userId; 

    /**
     * 0 未绑定;1 已绑定
     */
    private Integer bindingState; 

    /**
     * 暂无用到
     */
    private String externalAccount; 

    /**
     * 绑定时间
     */
    private LocalDateTime bindingTime; 

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

