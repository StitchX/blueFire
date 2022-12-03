package com.blue.business.converter;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.List;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/25 11:03]
 */
public class JacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    public class NullStringJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(StringUtils.EMPTY);
        }
    }

    public class NullBooleanJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeBoolean(false);
        }
    }

    public class LongJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toString());
        }
    }

    public class MyBeanSerializerModifier extends BeanSerializerModifier {
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDescription,
                                                         List<BeanPropertyWriter> beanPropertyWriters){
            for(Object beanProperty: beanPropertyWriters){
                BeanPropertyWriter writer = (BeanPropertyWriter) beanProperty;
                if(isStringType(writer)){
                    writer.assignNullSerializer(new NullStringJsonSerializer());
                }else if(isBooleanType(writer)){
                    writer.assignNullSerializer(new NullBooleanJsonSerializer());
                }else if(isLongType(writer)){
                    writer.assignSerializer(new LongJsonSerializer());
                }
            }
            return beanPropertyWriters;
        }
    }

    private boolean isStringType(BeanPropertyWriter writer){
        Class<?> clazz = writer.getType().getRawClass();
        return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
    }

    private boolean isBooleanType(BeanPropertyWriter writer){
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(Boolean.class);
    }

    private boolean isLongType(BeanPropertyWriter writer){
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(Long.class);
    }

    public JacksonHttpMessageConverter() {
        getObjectMapper().setSerializerFactory(getObjectMapper().getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
    }
}
