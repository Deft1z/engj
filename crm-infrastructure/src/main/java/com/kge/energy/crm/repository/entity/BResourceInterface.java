package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 资源接口表(BResourceInterface)实体类
 *
 * @author wangjihua
 * @since 2024-07-29 16:33:13
 */
@Data
@Accessors(chain = true)
public class BResourceInterface {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 资源id
     */
    private Integer resourceId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口地址
     */
    private String interfaceUrl;

    /**
     * 请求方式（GET POST PUT DELETE）
     */
    private String requestMethod;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 数据状态：-1-删除，1-正常
     */
    private Integer flag;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId;

    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime;
}

