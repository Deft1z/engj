package com.kge.energy.crm.file.controller;

import com.kge.energy.crm.file.req.GetFileIdByPathReq;
import com.kge.energy.crm.file.resp.GetFileIdByPathResp;
import com.kge.energy.crm.file.service.FileService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjihua
 */
@Tag(name = "文件API")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "文件路径获取文件ID")
    @PostMapping("/external/file/getFileIdByPath")
    public CommonResult<GetFileIdByPathResp> getFileIdByPath(@Validated @RequestBody GetFileIdByPathReq req) {
        return CommonResult.suc(fileService.getFileIdByPath(req));
    }

}
