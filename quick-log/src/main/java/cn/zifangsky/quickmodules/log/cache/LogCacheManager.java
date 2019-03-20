package cn.zifangsky.quickmodules.log.cache;

import cn.zifangsky.quickmodules.log.model.SysLog;

import java.util.List;

/**
 * 日志缓存抽象类
 *
 * @author zifangsky
 * @date 2017/12/13
 * @since 1.0.0
 */
public interface LogCacheManager {

    /**
     * 添加日志到缓存
     * @author zifangsky
     * @date 2017/12/13 11:24
     * @since 1.0.0
     * @param log 一条日志记录
     */
    void addCache(SysLog log);

    /**
     * 获取所有缓存的日志
     * @author zifangsky
     * @date 2017/12/13 11:32
     * @since 1.0.0
     * @return java.util.List<cn.zifangsky.quickmodules.log.model.SysLog>
     */
    List<SysLog> getAllCache();

    /**
     * 清空缓存里的日志
     * @author zifangsky
     * @date 2017/12/13 18:01
     * @since 1.0.0
     */
    void clearCache();
}
