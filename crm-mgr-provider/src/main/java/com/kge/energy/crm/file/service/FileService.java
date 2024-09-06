package com.kge.energy.crm.file.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.file.req.GetFileIdByPathReq;
import com.kge.energy.crm.file.resp.GetFileIdByPathResp;
import com.kge.energy.crm.repository.entity.SFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangjihua
 */
@Slf4j
@Service
public class FileService {

    /**
     * 文件路径获取文件ID
     */
    public GetFileIdByPathResp getFileIdByPath(GetFileIdByPathReq req) {

        SFile sFile = new LambdaQueryChainWrapper<>(SFile.class)
                .eq(SFile::getFilepath, req.getPath())
                .one();

        Assert.notNull(sFile, "文件不存在");

        return new GetFileIdByPathResp()
                .setFileId(sFile.getFileId())
                .setFilepath(sFile.getFilepath());
    }
}
