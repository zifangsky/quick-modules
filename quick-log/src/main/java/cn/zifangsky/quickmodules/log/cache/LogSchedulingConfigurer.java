package cn.zifangsky.quickmodules.log.cache;

import cn.zifangsky.quickmodules.common.utils.DateUtils;
import cn.zifangsky.quickmodules.log.mapper.LogMapper;
import cn.zifangsky.quickmodules.log.model.SysLog;
import cn.zifangsky.quickmodules.log.plugins.WebLogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

/**
 * 日志缓存定时消费
 *
 * @author zifangsky
 * @date 2017/12/13
 * @since 1.0.0
 */
@Component
public class LogSchedulingConfigurer implements SchedulingConfigurer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WebLogInfo logInfo;

    @Autowired
    private LogMapper logMapper;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        LogCacheManager cacheManager = logInfo.getCacheManager();

        //执行插入任务
        taskRegistrar.addCronTask(()->{
            logger.debug(MessageFormat.format("日志定时插入任务开始执行，当前时间：{0}", DateUtils.nowStr()));

            //获取缓存的所有日志
            List<SysLog> list = cacheManager.getAllCache();
            //清空缓存
            cacheManager.clearCache();

            int listSize = list.size();
            //每组数据量
            int groupSize = 100;

            for (int i = 0; i < listSize; i += groupSize) {
                //如果最后没有100条数据，则剩余几条是几条
                if (i + groupSize > listSize) {
                    groupSize = listSize - i;
                }
                List<SysLog> groupList = list.subList(i, i + groupSize);
                //批量插入到数据库
                logMapper.insertBatchSelective(groupList);
            }

            logger.debug(MessageFormat.format("日志定时插入任务执行完成，数据量：{0}，当前时间：{1}", listSize, DateUtils.nowStr()));
        }, logInfo.getScheduledCron());

    }
}
