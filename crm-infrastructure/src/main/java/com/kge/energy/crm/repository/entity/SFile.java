package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 文件(SFile)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
public class SFile {

    /**
     * 文件表
     */
    @TableId(type = IdType.AUTO)
    private Integer fileId; 

    /**
     * 外部引用类型（暂无使用）
     */
    private String referType; 

    /**
     * 外部引用ID（暂无使用）
     */
    private Integer referId; 

    /**
     * 文件名（路径）
     */
    private String filepath; 

    /**
     * 文件扩展名
     */
    private String extension; 

    /**
     * 文件MD5（与路径相关）
     */
    private String md5; 

    /**
     * 近一个月访问次数（停用）
     */
    private Integer visitCountRecent; 

    /**
     * 上次访问时间（停用）
     */
    private LocalDateTime visitTimeLast; 

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 

    /**
     * 软删除标识
     */
    private Integer flag; 

    /**
     * 文件真实名称
     */
    private String remark; 
}

