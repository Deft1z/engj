package com.kge.energy.crm.common.go;

import cn.hutool.core.util.ObjUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kge.platform.framework.common.util.ThreadLocalUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author wangjihua
 */
@Component
public class RoutingHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private final ConvertToGoFormatsConverter convertToGoFormatsConverter;

    public RoutingHttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
        convertToGoFormatsConverter = new ConvertToGoFormatsConverter();
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        if (useConvertToGoFormatsConverter()) {
            return convertToGoFormatsConverter.read(type, contextClass, inputMessage);
        }

        return super.read(type, contextClass, inputMessage);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        if (useConvertToGoFormatsConverter()) {
            return convertToGoFormatsConverter.readInternal(clazz, inputMessage);
        }

        return super.readInternal(clazz, inputMessage);
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        if (useConvertToGoFormatsConverter()) {
            convertToGoFormatsConverter.writeInternal(object, type, outputMessage);
        } else {
            super.writeInternal(object, type, outputMessage);
        }
    }

    /**
     * 是否使用 Go 格式转换器
     */
    private boolean useConvertToGoFormatsConverter() {

        Object isConvertToGoFormats = ThreadLocalUtils.get("IS_CONVERT_TO_GO_FORMATS");

        return ObjUtil.isNotNull(isConvertToGoFormats) && ObjUtil.equals(isConvertToGoFormats, Boolean.TRUE);
    }
}

