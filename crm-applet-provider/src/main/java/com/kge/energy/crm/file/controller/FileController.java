package com.kge.energy.crm.file.controller;

import com.kge.energy.crm.file.resp.UploadFileResp;
import com.kge.energy.crm.file.service.FileService;
import com.kge.platform.framework.common.net.CommonResult;
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
    public CommonResult<UploadFileResp> uploadFileProxy(@RequestParam("file") MultipartFile file) {
        return CommonResult.suc(fileService.uploadFileProxy(file));
    }

    @Operation(summary = "上传临时文件，会被定时清除", description = "只适用单节点部署限制")
    @PostMapping("/file/uploadTmpFile")
    public CommonResult<String> uploadTmpFile(@RequestParam("file") MultipartFile file,
                                              @RequestParam(name = "bizType") String bizType) {
        return CommonResult.suc(fileService.uploadTmpFile(file, bizType));
    }
}
