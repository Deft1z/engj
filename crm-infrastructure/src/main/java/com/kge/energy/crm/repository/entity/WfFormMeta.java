package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 表单元数据(WfFormMeta)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
public class WfFormMeta {

    @TableId(type = IdType.AUTO)
    private Integer formMetaId; 

    private Integer formTypeId; 

    private String version; 

    private String meta; 

    private Integer flag; 

    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId; 

    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime; 

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId; 

    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 

    private String remark; 
}

