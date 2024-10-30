package com.kge.energy.crm.file.service;

import com.kge.energy.crm.file.resp.UploadFileResp;
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

    public String uploadTmpFile(MultipartFile file) {
        return fileDomainService.uploadTmpFile(file);
    }
}
