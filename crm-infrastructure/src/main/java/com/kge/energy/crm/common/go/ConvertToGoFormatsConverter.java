package com.kge.energy.crm.common.go;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wangjihua
 */
public class ConvertToGoFormatsConverter extends MappingJackson2HttpMessageConverter {

    public ConvertToGoFormatsConverter(ObjectMapper objectMapper) {
        super(goFormatsObjectMapper(objectMapper));
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        super.writeInternal(object, type, outputMessage);
    }

    public static ObjectMapper goFormatsObjectMapper(ObjectMapper mapper) {

        DefaultSerializerProvider provider = (DefaultSerializerProvider) mapper.getSerializerProvider();

        DefaultDeserializationContext context = (DefaultDeserializationContext) mapper.getDeserializationContext();

        SimpleModule module = new SimpleModule()
                .setSerializerModifier(new MyBeanSerializerModifier());

        return new ObjectMapper(mapper.getFactory(), provider, context)
                .registerModule(module)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    private static class MyBeanSerializerModifier extends BeanSerializerModifier {

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

            for (int i = 0; i < beanProperties.size(); i++) {

                BeanPropertyWriter writer = beanProperties.get(i);

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
