package cn.zifangsky.quickmodules.log.config;

import cn.zifangsky.quickmodules.common.annotations.ConditionalOnBeanUndefined;
import cn.zifangsky.quickmodules.log.plugins.DbUtilsTemplate;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Web相关配置
 * @author zifangsky
 * @date 2017/12/3
 * @since 1.0.0
 */
@Configuration
@ComponentScan(basePackages= {"cn.zifangsky.quickmodules.log"})
public class WebConfiguration {

    /**
     * DButils查询数据自动转驼峰格式
     * @author zifangsky
     * @date 2017/11/6 19:17
     * @since 1.0.0
     * @return org.apache.commons.dbutils.RowProcessor
     */
    @Bean
    @ConditionalOnBeanUndefined(RowProcessor.class)
    public RowProcessor rowProcessor(){
        BeanProcessor beanProcessor = new GenerousBeanProcessor();
        return new BasicRowProcessor(beanProcessor);
    }

    /**
     * 添加自定义的dbUtils框架的底层查询封装
     * @author zifangsky
     * @date 2017/11/15 15:28
     * @since 1.0.0
     * @return cn.zifangsky.quickmodules.common.plugins.DbUtilsTemplate
     */
    @Bean("logDbUtilsTemplate")
    public DbUtilsTemplate dbUtilsTemplate(DataSource dataSource, RowProcessor rowProcessor){
        return new DbUtilsTemplate(dataSource, rowProcessor);
    }

}
