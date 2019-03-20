package cn.zifangsky.quickmodules.log.common;

/**
 * 公共常量类
 *
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
public interface Constants {

    //Table names
    String TABLE_LOG = "sys_log";

    /**
     * 默认的定时消费缓存日志的<b>Cron</b>表达式（每隔2分钟一次）
     */
    String DEFAULT_SCHEDULED_CRON="0 */2 * * * ?";

}
