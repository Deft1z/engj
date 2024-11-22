package com.kge.energy.crm.qrcode;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final QrConfig config;

    /**
     * 生成到文件
     *
     * @param content
     * @param filePath
     */
    @SneakyThrows
    public void createCodeToFile(String content, String filePath) {
        QrCodeUtil.generate(content, config, FileUtil.file(filePath));
    }

    /**
     * 生成到流
     *
     * @param content
     * @param response
     */
    @SneakyThrows
    public void createCodeToStream(String content, HttpServletResponse response) {
        QrCodeUtil.generate(content, config, "png", response.getOutputStream());
    }

    /**
     * 生成为base64图片编码
     * @param content
     * @return
     */
    public String createCodeToBase64Img(String content) {
        return "data:image/png;base64," + createCodeToBase64(content);
    }

    @SneakyThrows
    public String createCodeToBase64(String content) {
        BufferedImage image = QrCodeUtil.generate(content, config);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);
        return Base64.encode(stream.toByteArray());
    }

}