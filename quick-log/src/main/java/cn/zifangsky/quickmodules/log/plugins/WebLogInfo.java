package cn.zifangsky.quickmodules.log.plugins;

import cn.zifangsky.quickmodules.log.cache.LogCacheManager;
import cn.zifangsky.quickmodules.log.cache.impl.MemoryLogCacheManager;
import cn.zifangsky.quickmodules.log.common.Constants;

/**
 * 配置基本信息
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public class WebLogInfo {
    /**
     * 通过角色查询日志的SQL前缀
     */
    private String selectByRolePrefixSql;

    /**
     * 对外暴露出去的方法
     */
    private AbstractLogManager logManager;

    /**
     * 是否启动日志缓存
     */
    private boolean enableCache;

    /**
     * 日志缓存管理器
     */
    private LogCacheManager cacheManager;

    /**
     * 定时消费缓存日志的<b>Cron</b>表达式
     */
    private String scheduledCron;

    public WebLogInfo(WebLogInfo.Builder builder){
        this.selectByRolePrefixSql = builder.selectByRolePrefixSql;
        this.logManager = builder.logManager;
        this.enableCache = builder.enableCache;
        this.cacheManager = builder.cacheManager;
        this.scheduledCron = builder.scheduledCron;
    }

    public String getSelectByRolePrefixSql() {
        return selectByRolePrefixSql;
    }

    public boolean isEnableCache() {
        return enableCache;
    }

    public AbstractLogManager getLogManager() {
        return logManager;
    }

    public LogCacheManager getCacheManager() {
        return cacheManager;
    }

    public String getScheduledCron() {
        return scheduledCron;
    }

    /**
     * 构建类
     */
    public static class Builder {
        /**
         * 通过角色查询日志的SQL前缀
         */
        private String selectByRolePrefixSql = "user_id IN(SELECT sys_user.id FROM sys_user,sys_user_role WHERE sys_user.id = sys_user_role.user_id AND sys_user_role.role_id = ?)";

        /**
         * 对外暴露出去的方法
         */
        private AbstractLogManager logManager;

        /**
         * 是否启动日志缓存
         */
        private boolean enableCache = false;

        /**
         * 日志缓存管理器
         */
        private LogCacheManager cacheManager;

        /**
         * 定时消费缓存日志的<b>Cron</b>表达式
         */
        private String scheduledCron;

        /**
         * 通过角色查询日志的SQL前缀
         * <p>默认用户表为：sys_user，默认用户角色表为：sys_user_role</p>
         */
        public Builder selectByRolePrefixSql(String selectByRolePrefixSql) {
            this.selectByRolePrefixSql = selectByRolePrefixSql;
            return this;
        }

        /**
         * 设置自定义日志管理类，用于复写后获取用户等信息
         */
        public Builder logManager(AbstractLogManager logManager) {
            this.logManager = logManager;
            return this;
        }

        /**
         * 是否启动日志缓存
         * <p>默认：false</p>
         */
        public Builder enableCache(boolean enableCache) {
            this.enableCache = enableCache;
            return this;
        }

        /**
         * 设置日志缓存管理器
         * <p>当<b>enableCache</b>参数生效时才生效，默认实现采用内存缓存，即：{@link cn.zifangsky.quickmodules.log.cache.impl.MemoryLogCacheManager}</p>
         */
        public Builder cacheManager(LogCacheManager cacheManager) {
            this.cacheManager = cacheManager;
            return this;
        }

        /**
         * 定时消费缓存日志的<b>Cron</b>表达式
         * <p>默认：0 *\/2 * * * ? ，也就是每隔2分钟消费一次</p>
         */
        public Builder scheduledCron(String scheduledCron) {
            this.scheduledCron = scheduledCron;
            return this;
        }

        public WebLogInfo build(){
            if(this.cacheManager == null){
                this.cacheManager = new MemoryLogCacheManager();
            }
            if(this.scheduledCron == null){
                this.scheduledCron = Constants.DEFAULT_SCHEDULED_CRON;
            }

            return new WebLogInfo(this);
        }
    }

}
