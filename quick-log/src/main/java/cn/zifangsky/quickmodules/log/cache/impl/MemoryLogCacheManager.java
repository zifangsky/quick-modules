package cn.zifangsky.quickmodules.log.cache.impl;

import cn.zifangsky.quickmodules.log.cache.LogCacheManager;
import cn.zifangsky.quickmodules.log.model.SysLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 内存中缓存
 *
 * @author zifangsky
 * @date 2017/12/13
 * @since 1.0.0
 */
public class MemoryLogCacheManager implements LogCacheManager {
    private final ConcurrentLinkedQueue<SysLog> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void addCache(SysLog log) {
        if(log != null){
            //将日志添加到队列
            queue.add(log);
        }
    }

    @Override
    public List<SysLog> getAllCache() {
        List<SysLog> list = new ArrayList<>(100);
        SysLog temp;

        //队列元素依次出队，获取缓存的所有日志
        while ((temp = queue.poll()) != null){
            list.add(temp);
        }

        return list;
    }

    @Override
    public void clearCache() {
        //ignore
    }
}
