package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * wx_pages 微信小程序页面(WxPages)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:25
 */
@Data
@Accessors(chain = true)
public class WxPages {

    @TableId(type = IdType.AUTO)
    private Integer pagesId; 

    private String name; 

    private String path; 

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

