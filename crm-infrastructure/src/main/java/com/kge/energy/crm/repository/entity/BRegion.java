package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 行政区域(BRegion)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:25
 */
@Data
@Accessors(chain = true)
public class BRegion {

    @TableId(type = IdType.AUTO)
    private Integer regionId; 

    /**
     * 上一级区域ID
     */
    private Integer parentRegionId; 

    /**
     * 省
;市;区

     */
    private Integer level; 

    /**
     * 停用
     */
    private String authCode; 

    /**
     * 名称
     */
    private String name; 

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

