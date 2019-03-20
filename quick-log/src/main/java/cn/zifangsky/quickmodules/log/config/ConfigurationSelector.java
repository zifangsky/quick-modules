package cn.zifangsky.quickmodules.log.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过实现{@link ImportSelector}接口引入配置类
 *
 * @author zifangsky
 * @date 2017/12/4
 * @since 1.0.0
 */
public class ConfigurationSelector implements ImportSelector {

    /**
     * 组装需要返回的配置类
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        importingClassMetadata.getAnnotationTypes().forEach(System.out::println);

        List<String> result = new ArrayList<>(2);
        //添加两个配置类
        result.add(WebConfiguration.class.getName());
        result.add(LogConfiguration.class.getName());

        return result.toArray(new String[0]);
    }

}
