package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 用户令牌(LUserToken)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:25
 */
@Data
@Accessors(chain = true)
public class LUserToken {

    @TableId(type = IdType.AUTO)
    private Integer userTokenId; 

    /**
     * 用户ID
     */
    private Integer userId; 

    private Integer loginOrganizationId; 

    /**
     * 停用
     */
    private String terminalType; 

    /**
     * 停用
     */
    private String terminalAddress; 

    /**
     * 登录口令
     */
    private String loginToken; 

    /**
     * 登录过期时间
     */
    private LocalDateTime loginExpiredTime; 

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 
}

