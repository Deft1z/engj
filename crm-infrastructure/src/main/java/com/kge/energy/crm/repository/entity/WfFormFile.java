package com.kge.energy.crm.repository.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 表单附件(WfFormFile)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class WfFormFile {

    @TableId(type = IdType.AUTO)
    private Integer wfFormFileId; 

    private Integer formId; 

    private Integer formFlowId; 

    private Integer fileId; 
}

