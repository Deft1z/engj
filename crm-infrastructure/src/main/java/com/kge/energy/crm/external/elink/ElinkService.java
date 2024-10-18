package com.kge.energy.crm.external.elink;

import com.kge.platform.framework.web.util.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjihua
 */
@Slf4j
@Service
public class ElinkService {

    public String createElinkPushContent(String msgId, String head, String content, String phone) {
        return createElinkPushContent(msgId, head, content, List.of(phone));
    }

    public String createElinkPushContent(String msgId, String head, String content, List<String> phones) {
        String template = "<XML><APIKEY>5444414F56537A5E7262585B5C</APIKEY><ID>%s</ID><HEAD>%s</HEAD><TITLE>%s</TITLE>%s<toMsg>1</toMsg><toWX>1</toWX></XML>";

        String sendPhones = phones.stream().map(phone -> "<PHONE ISONLINE='0'>" + phone + "</PHONE>")
                .collect(Collectors.joining(""));

        return String.format(template, msgId, head, content, sendPhones);
    }

    /**
     * 格式：<XML><APIKEY>5444414F56537A5E7262585B5C</APIKEY><ID>%s</ID><HEAD>%s</HEAD><TITLE>%s</TITLE><PHONE ISONLINE='0'>%s</PHONE><PHONE ISONLINE='0'>%s</PHONE><toMsg>1</toMsg><toWX>1</toWX></XML>
     * 内容换行加 \\n
     * 周兴祥：单次最好是一个手机号。多手机号会出现其中一个出错全部重发的情况。
     */
    public String pushElinkMsg(String content) {

        String url = "http://172.18.52.222:6669/MessageToWX";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml;charset=UTF-8");
        return RestUtils.postForObject(url, headers, content, String.class);
    }
}
