package cn.zifangsky.quickmodules.user.enums;

import java.util.concurrent.TimeUnit;

/**
 * 过期时间相关枚举
 * @author zifangsky
 * @date 2017/11/6 17:05
 * @since 1.0.0
 */
public enum ExpireTypes {
    //默认短信验证码的有效期为1分钟
    DEFAULT_PHONE_CODE(60L, TimeUnit.SECONDS),
    //Shiro的session的全局过期时间（2小时）
    SHIRO_SESSION(7200000L, TimeUnit.MILLISECONDS)
    ;

    /**
     * 过期时间
     */
    private Long time;
    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    ExpireTypes(Long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public Long getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
