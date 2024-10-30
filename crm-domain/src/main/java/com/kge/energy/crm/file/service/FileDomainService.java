package com.kge.energy.crm.file.service;

import cn.hutool.core.lang.Assert;
import com.kge.energy.crm.external.file.service.FileInfraService;
import com.kge.energy.crm.file.resp.UploadFileResp;
import com.kge.energy.crm.repository.dao.SFileDao;
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
public class FileDomainService {

    private final FileInfraService fileInfraService;

    private final SFileDao sFileDao;

    public UploadFileResp uploadFile(MultipartFile file) {

        String filePath = fileInfraService.uploadFile(file);

        SFile sFile = sFileDao.getByFilePath(filePath);
        Assert.notNull(sFile, "文件不存在");

        return new UploadFileResp()
                .setFileId(sFile.getFileId())
                .setFilePath(sFile.getFilepath());
    }

    public String uploadTmpFile(MultipartFile file, String bizType) {
        return fileInfraService.uploadTmpFile(file, bizType);
    }
}
