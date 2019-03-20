package cn.zifangsky.quickmodules.log.annotation;

import cn.zifangsky.quickmodules.log.config.ConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用日志记录功能
 * @author zifangsky
 * @date 2017/12/4
 * @since 1.0.0
 * @see ConfigurationSelector
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
@Documented
@Import({ConfigurationSelector.class})
public @interface EnableWebLog {

}
