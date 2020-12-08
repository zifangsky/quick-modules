package cn.zifangsky.quickmodules.common.annotations;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface PropertySourcedMapping {
    String propertyKey();
}
