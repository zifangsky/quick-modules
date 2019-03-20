package cn.zifangsky.quickmodules.log.annotation;

import cn.zifangsky.quickmodules.log.enums.LogTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebLog {

    /**
     * 日志正文部分（支持类SPEL表达式）
     */
    String content() default "";
    /**
     * 日志类型
     */
    LogTypes type() default LogTypes.OPERATION;
    /**
     * 当前模块（可以是一个json字符串，支持类SPEL表达式）
     */
    String module() default "";

    /**
     * 打印日志的条件（类SPEL表达式）
     */
    String condition() default "";

    /**
     * 打印日志的条件（跟参数 condition 的含义刚好相反）
     */
    String unless() default "";

}
