package cn.zifangsky.quickmodules.common.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Java Bean相关公共方法
 *
 * @author zifangsky
 * @date 2020/12/7
 * @since 1.1.0
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

    /**
     * 判断某个类是否包含指定字段
     * @author zifangsky
     * @date 2020/7/7 14:33
     * @since 1.0.0
     * @param object object
     * @param fieldName fieldName
     * @return boolean
     */
    public static boolean whetherContainField(Object object, String fieldName){
        try {
            object.getClass().getDeclaredField(fieldName);
            return true;
        }catch (NoSuchFieldException e){
            return false;
        }
    }

    /**
     * 如果对象的某个字段为null，则为其设置不为空的默认值（注：只检查 String/Integer/Long/Float/Double 这几种格式）
     * @author zifangsky
     * @date 2020/8/26 10:15
     * @since 1.0.0
     * @param element 待处理对象
     */
    public static void setDefaultValueForEmptyFields(Object element) {
        //1. 获取当前类及父类的所有属性
        List<Field> fieldList = getObjectAllFields(element);

        //2. 统一处理某几个格式的属性
        for (Field field : fieldList) {
            //更改权限
            field.setAccessible(true);

            try {
                //处理空值的属性
                if (field.get(element) == null) {
                    String type = field.getType().getName();
                    switch (type) {
                        case "java.lang.String":
                            field.set(element, "none");
                            break;
                        case "java.lang.Integer":
                            field.set(element, 0);
                            break;
                        case "java.lang.Long":
                            field.set(element, 0L);
                            break;
                        case "java.lang.Float":
                            field.set(element, 0.0F);
                            break;
                        case "java.lang.Double":
                            field.set(element, 0.0D);
                            break;
                        default:
                            break;
                    }
                }
            }catch (Exception e){
                //ignore
            }
        }
    }

    /**
     * 统一将空字符串属性设置为Null
     * @author zifangsky
     * @date 2020/8/26 10:19
     * @since 1.0.0
     * @param element 待处理对象
     */
    public static <K> K setEmptyStringFieldToNullUniformly(K element){
        if(element == null){
            return null;
        }

        Class<?> clazz = element.getClass();

        //1. 获取当前类及父类的所有属性
        List<Field> fieldList = getObjectAllFields(element);

        //2. 统一处理所有符合条件的属性
        for (Field field : fieldList) {
            //更改权限
            field.setAccessible(true);

            try {
                //判断字段是否属于字符串
                if(field.getType().isAssignableFrom(String.class)){
                    String fValue = (String) field.get(element);

                    //如果字段为空字符串，则设置为null
                    if(fValue != null && "".equals(fValue.trim())){
                        field.set(element, null);
                    }
                }
            }catch (Exception e){
                //ignore
            }
        }

        return element;
    }

    /**
     * 获取某个对象的当前类及父类的所有属性
     */
    public static List<Field> getObjectAllFields(Object element){
        if(element == null){
            return null;
        }

        Class<?> clazz = element.getClass();

        //获取当前类及父类的所有属性
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            Field[] declaredFields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(declaredFields));

            clazz = clazz.getSuperclass();
        }

        return fieldList;
    }
}