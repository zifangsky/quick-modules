package cn.zifangsky.quickmodules.common.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Java Bean相关公共方法
 *
 * @author zifangsky
 * @date 2017/11/14 0014
 * @since 1.0.0
 */
public class BeanUtils {

    /**
     * 复制Bean的参数
     * @author zifangsky
     * @date 2017/11/14 13:44
     * @since 1.0.0
     * @param source source
     * @param target target
     */
    public static void copyProperties(final Object source, final Object target) throws Exception {
        BeanUtilsBean.getInstance().copyProperties(target, source);
    }

    /**
     * 通过参数名获取参数值
     * @author zifangsky
     * @date 2017/11/14 13:58
     * @since 1.0.0
     * @param bean Bean
     * @param name 参数名
     * @return java.lang.Object
     */
    public static Object getProperty(final Object bean, final String name) throws Exception {
        return BeanUtilsBean.getInstance().getPropertyUtils().getNestedProperty(bean, name);
    }

    /**
     * 通过参数名获取参数值
     * @author zifangsky
     * @date 2017/11/14 13:58
     * @since 1.0.0
     * @param bean Bean
     * @param name 参数名
     * @return java.lang.Object
     */
    public static Object getSimpleProperty(final Object bean, final String name) throws Exception {
        return BeanUtilsBean.getInstance().getPropertyUtils().getSimpleProperty(bean, name);

    }

    /**
     * 将Map转Bean
     * @author zifangsky
     * @date 2017/11/14 14:03
     * @since 1.0.0
     * @param clazz Bean的类型
     * @param properties 所有参数
     * @return K
     */
    public static <K> K mapToObject(Class<K> clazz, final Map<String, ? extends Object> properties) throws Exception {
        if(properties == null){
            return null;
        }else {
            K k = clazz.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(k, properties);

            return k;
        }
    }

    /**
     * 将Bean的所有参数转换为Map
     * @author zifangsky
     * @date 2017/11/14 14:21
     * @since 1.0.0
     * @param bean Bean
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public static Map<String, Object> objectToMap(Object bean) throws Exception {
        if(bean == null){
            return null;
        }else{
            Field[] allFields = FieldUtils.getAllFields(bean.getClass());
            Map<String, Object> map = new HashMap<>(allFields.length);

            for (Field field : allFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(bean));
            }

            return map;
        }
    }

}
