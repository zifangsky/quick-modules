package cn.zifangsky.quickmodules.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 基于 Jackson 的工具类
 *
 * @author zifangsky
 * @date 2020/12/7
 * @since 1.1.0
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串
     */
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("'将对象转换成json字符串'出现异常", e);
        }
        return null;
    }

    /**
     * 将json字符串转化为对象
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            log.error("'将json字符串转化为对象'出现异常", e);
        }
        return null;
    }

    /**
     * 将json字符串转换成对象list
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.error("'将json字符串转换成对象list'出现异常", e);
        }
        return null;
    }

}
