package com.kge.energy.crm.file.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.file.resp.UploadFileResp;
import com.kge.energy.crm.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wangjihua
 */
@Tag(name = "文件API")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "上传文件")
    @PostMapping("/external/file/uploadFileProxy")
    public CommonResponse<UploadFileResp> uploadFileProxy(@RequestParam("file") MultipartFile file) {
        return CommonResponse.suc(fileService.uploadFileProxy(file));
    }

}
