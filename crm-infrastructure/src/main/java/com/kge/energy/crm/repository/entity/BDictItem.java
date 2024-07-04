package com.kge.energy.crm.repository.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * b_dict_item 字典项表(BDictItem)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class BDictItem {

    /**
     * 字典项表，暂无用到
     */
    @TableId(type = IdType.AUTO)
    private Integer dictItemId; 

    /**
     * 字典表
     */
    private Integer dictId; 

    /**
     * 字典项名称
     */
    private String label; 

    /**
     * 字典项值
     */
    private String value; 

    /**
     * 排序
     */
    private Integer sort; 
}

