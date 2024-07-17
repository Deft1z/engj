package com.kge.energy.crm.common.go;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author wangjihua
 */
public class ConvertToGoFormatsConverter extends MappingJackson2HttpMessageConverter {

    public ConvertToGoFormatsConverter() {
        super(goFormatsObjectMapper());
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return super.readInternal(clazz, inputMessage);
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        super.writeInternal(object, type, outputMessage);
    }

    private static ObjectMapper goFormatsObjectMapper() {

        SimpleModule module = new SimpleModule()
                .setSerializerModifier(new MyBeanSerializerModifier())
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return JsonMapper.builder()
                .addModule(module)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    private static class MyBeanSerializerModifier extends BeanSerializerModifier {

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

            for (BeanPropertyWriter writer : beanProperties) {

                if (isStringType(writer)) {
                    writer.assignNullSerializer(new NullStringSerializer());
                }

                if (isNumericType(writer)) {
                    writer.assignNullSerializer(new NullNumberSerializer());
                }
            }

            return super.changeProperties(config, beanDesc, beanProperties);
        }

        protected boolean isStringType(BeanPropertyWriter writer) {

            Class clazz = writer.getPropertyType();
            return clazz.equals(String.class);
        }

        protected boolean isNumericType(BeanPropertyWriter writer) {
            Class clazz = writer.getPropertyType();
            return clazz != null &&
                    (Integer.class.isAssignableFrom(clazz) ||
                            Long.class.isAssignableFrom(clazz) ||
                            Short.class.isAssignableFrom(clazz) ||
                            Byte.class.isAssignableFrom(clazz) ||
                            Double.class.isAssignableFrom(clazz) ||
                            Float.class.isAssignableFrom(clazz) ||
                            Number.class.isAssignableFrom(clazz));

        }

    }


    private static class NullStringSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value == null) {
                gen.writeString(""); // 将null的String转换为空字符串
            }
        }
    }

    private static class NullNumberSerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value == null) {
                gen.writeNumber(0); // 将null的Number转换为0
            }
        }
    }


}
