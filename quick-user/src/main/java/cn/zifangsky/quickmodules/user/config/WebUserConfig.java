package cn.zifangsky.quickmodules.user.config;

import cn.zifangsky.quickmodules.user.controller.WebLoginController;
import cn.zifangsky.quickmodules.user.controller.WebUserController;
import cn.zifangsky.quickmodules.user.plugins.DbUtilsTemplate;
import cn.zifangsky.quickmodules.user.plugins.PropertySourcedRequestMappingHandlerMapping;
import cn.zifangsky.quickmodules.user.plugins.WebUserInfo;
import cn.zifangsky.quickmodules.user.service.UserService;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerMapping;

import javax.sql.DataSource;

/**
 * Web相关配置
 * @author zifangsky
 * @date 2017/11/1
 * @since 1.0.0
 */
@Configuration
@Import({EasyLimitConfig.class})
@ComponentScan(basePackages= {"cn.zifangsky.quickmodules.user", "cn.zifangsky.quickmodules.common"})
public class WebUserConfig {

    /**
     * DButils查询数据自动转驼峰格式
     * @author zifangsky
     * @date 2017/11/6 19:17
     * @since 1.0.0
     * @return org.apache.commons.dbutils.RowProcessor
     */
    @Bean
    public RowProcessor rowProcessor(){
        BeanProcessor beanProcessor = new GenerousBeanProcessor();
        return new BasicRowProcessor(beanProcessor);
    }

    /**
     * 添加自定义的dbUtils框架的底层查询封装
     * @author zifangsky
     * @date 2017/11/15 15:28
     * @since 1.0.0
     * @return cn.zifangsky.quickmodules.user.plugins.DbUtilsTemplate
     */
    @Bean("userDbUtilsTemplate")
    public DbUtilsTemplate dbUtilsTemplate(DataSource dataSource, RowProcessor rowProcessor){
        return new DbUtilsTemplate(dataSource, rowProcessor);
    }

    /**
     * 添加一个自定义的{@link HandlerMapping}，用于修改请求路径
     */
    @Bean
    public HandlerMapping webLoginControllerMapping(WebUserInfo webUserInfo, UserService userService){
        return new PropertySourcedRequestMappingHandlerMapping(webUserInfo, new WebLoginController(userService));
    }

    /**
     * 添加一个自定义的{@link HandlerMapping}，用于修改请求路径
     */
    @Bean
    public HandlerMapping webUserControllerMapping(WebUserInfo webUserInfo, UserService userService){
        return new PropertySourcedRequestMappingHandlerMapping(webUserInfo, new WebUserController(userService));
    }
}
