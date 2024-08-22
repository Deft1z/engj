package com.kge.energy.crm.external.file.service;

import cn.hutool.core.io.FileUtil;
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
import java.util.Arrays;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileInfraService {

    private final FileProperty fileProperty;

    @SneakyThrows
    public String uploadFile(MultipartFile multipartFile) {


        String fileType = FileUtil.extName(multipartFile.getOriginalFilename());

        if (!Arrays.asList(fileProperty.getUpload().getAllowTypes().split(",")).contains(fileType)) {
            throw new ServiceException("不允许的文件类型：" + fileType);
        }

        String md5Code;
        try {
            md5Code = MD5.create().digestHex(multipartFile.getBytes());
        } catch (IOException e) {
            throw new ServiceException("获取文件MD5值异常");
        }

//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("sign", md5Code);
//        paramMap.put("file", multipartFile);

        File file = new File("D:\\test.png");
        multipartFile.transferTo(file);

        HttpRequest post = HttpUtil.createPost(fileProperty.getUpload().getUploadUrl());
        post.header(Header.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
        post.form("sign", md5Code);
        post.form("file", file);

        String response = post.execute().body();//HttpUtil.post(fileProperty.getUpload().getUploadUrl(), paramMap);

        log.info("上传文件 name: {}, md5Code: {}, 响应结果：{}", multipartFile.getOriginalFilename(), md5Code, response);

        String filePath = JSONUtil.parseObj(response).getStr("data");
        Assert.hasLength(filePath, "文件路径不存在");

        return filePath;
    }
}
