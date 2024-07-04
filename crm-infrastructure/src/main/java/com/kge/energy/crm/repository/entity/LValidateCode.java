package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 校验码(LValidateCode)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class LValidateCode {

    @TableId(type = IdType.AUTO)
    private Integer validateCodeId; 

    /**
     * 用户ID
     */
    private Integer userId; 

    /**
     * 终端类型
     */
    private String terminalType; 

    /**
     * 终端地址
     */
    private String terminalAddress; 

    private String type; 

    /**
     * 验证码
     */
    private String code; 

    /**
     * 过期时间
     */
    private LocalDateTime expiredTime; 

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 
}

