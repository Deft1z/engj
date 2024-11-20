package com.kge.energy.crm.file.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.file.req.GetFileIdByPathReq;
import com.kge.energy.crm.file.resp.GetFileIdByPathResp;
import com.kge.energy.crm.file.resp.UploadFileResp;
import com.kge.energy.crm.repository.entity.SFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileDomainService fileDomainService;

    public UploadFileResp uploadFileProxy(MultipartFile file) {
        return fileDomainService.uploadFile(file);
    }

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
