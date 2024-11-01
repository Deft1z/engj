package com.kge.energy.crm.external.file.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.kge.energy.crm.external.file.property.FileProperty;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileInfraService {

    private final FileProperty fileProperty;

    private void checkUploadFileType(MultipartFile multipartFile) {
        String fileType = FileUtil.extName(multipartFile.getOriginalFilename());

        if (!Arrays.asList(fileProperty.getUpload().getAllowTypes().split(",")).contains(fileType)) {
            throw new ServiceException("不允许的文件类型：" + fileType);
        }
    }

    @SneakyThrows
    public String uploadFile(MultipartFile multipartFile) {

        checkUploadFileType(multipartFile);

        String md5Code;
        try {
            md5Code = MD5.create().digestHex(multipartFile.getBytes());
        } catch (IOException e) {
            throw new ServiceException("获取文件MD5值异常");
        }

        // 创建临时文件
        File tempFile = null;
        String filePath;

        try {
            tempFile = new File(System.getProperty("java.io.tmpdir"), multipartFile.getOriginalFilename());
            multipartFile.transferTo(tempFile);

            HttpRequest post = HttpUtil.createPost(fileProperty.getUpload().getUploadUrl());
            post.header(Header.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
            post.form("sign", md5Code);
            post.form("file", tempFile);

            String response = post.execute().body();

            log.info("上传文件 name: {}, md5Code: {}, 响应结果：{}", tempFile.getName(), md5Code, response);

            filePath = JSONUtil.parseObj(response).getStr("data");
            Assert.hasLength(filePath, "文件路径不存在");

        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        return filePath;
    }

    /**
     * 创建临时文件
     */
    public String uploadTmpFile(MultipartFile multipartFile, String bizType) {

        checkUploadFileType(multipartFile);

        String resultPath;

        try {
            String filePath = fileProperty.getUpload().getTmpDir() + FileUtil.FILE_SEPARATOR +
                    bizType + FileUtil.FILE_SEPARATOR +
                    DateUtil.format(LocalDateTime.now(), DatePattern.SIMPLE_MONTH_PATTERN);
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = IdUtil.fastSimpleUUID() + "." + FileUtil.extName(multipartFile.getOriginalFilename());
            resultPath = FileUtil.FILE_SEPARATOR + bizType + FileUtil.FILE_SEPARATOR +
                    DateUtil.format(LocalDateTime.now(), DatePattern.SIMPLE_MONTH_PATTERN) + FileUtil.FILE_SEPARATOR + fileName;

            Path path = Paths.get(filePath, fileName);
            Files.copy(multipartFile.getInputStream(), path);

        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new ServiceException("上传文件失败");
        }

        return resultPath;
    }
}
