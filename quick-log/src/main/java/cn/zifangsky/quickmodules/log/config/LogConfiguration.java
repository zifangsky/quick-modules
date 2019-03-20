package cn.zifangsky.quickmodules.log.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 日志记录相关配置
 *
 * @author zifangsky
 * @date 2017/12/4
 * @since 1.0.0
 */
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
@Configuration
public class LogConfiguration {

    @Bean("logTaskExecutor")
    public TaskExecutor executor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("quick-log-");
        //线程池维护线程的最少数量
        taskExecutor.setCorePoolSize(5);
        //线程池维护线程的最大数量
        taskExecutor.setMaxPoolSize(10);
        //线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(25);
        //设置拒绝策略（线程池满了，则丢弃新加入的任务，并抛出异常）
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.initialize();

        return taskExecutor;
    }

}
