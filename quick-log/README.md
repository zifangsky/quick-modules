## 模块介绍 ##

此模块用于在基于`Spring`的项目中通过简单注解即可在数据库灵活存储用户访问的操作日志/业务日志。



## 开始使用 ##

#### （1）引入依赖： ####

```json
<dependency>
	<groupId>cn.zifangsky.quickmodules</groupId>
	<artifactId>quick-log</artifactId>
	<version>${quick-log}</version>
</dependency>
```



#### （2）在数据库中新建如下的表： ####

```mysql
-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) DEFAULT NULL COMMENT '日志正文（可以存放json字符串）',
  `type` int(11) DEFAULT '0' COMMENT '日志类型（0：操作日志；1：业务日志）',
  `module` varchar(255) DEFAULT NULL COMMENT '当前模块（可以存放json字符串）',
  `user_id` bigint(11) DEFAULT NULL COMMENT '当前用户ID',
  `username` varchar(255) DEFAULT NULL COMMENT '当前用户名（中文名）',
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录日志的开始时间',
  `take_time` bigint(20) DEFAULT NULL COMMENT '执行目标方法花费总时间（单位毫秒）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COMMENT='操作/业务日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('101', '在controller中执行 selectByUsername 方法，查询用户名：admin', '0', '', '1', '超级管理员', '2019-03-20 17:34:56', '63');
INSERT INTO `sys_log` VALUES ('102', '在controller中执行 selectByUsername 方法，查询用户名：admin', '0', '', '1', '超级管理员', '2019-03-20 17:35:34', '9408');
INSERT INTO `sys_log` VALUES ('103', '在controller中执行 selectByUsername 方法，查询用户名：admin', '0', '', '1', '超级管理员', '2019-03-20 17:47:16', '4080');

```



#### （3）在项目中添加相应配置： ####

```java
package cn.zifangsky.quickmodules.example.config;

import cn.zifangsky.quickmodules.log.annotation.EnableWebLog;
import cn.zifangsky.quickmodules.log.plugins.AbstractLogManager;
import cn.zifangsky.quickmodules.log.plugins.WebLogInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 测试使用quick-log模块
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
@Configuration
@EnableWebLog
public class QuickLogModuleConfig {

    @Bean
    public WebLogInfo webLogInfo(){
        WebLogInfo.Builder builder = new WebLogInfo.Builder();
        AbstractLogManager logManager = new LogManager();

        return builder
                //启动日志缓存
                .enableCache(true)
                //自定义LogManager
                .logManager(logManager)
                .build();
    }

}
```



#### （4）正式使用： ####

在添加上述配置后，`quick-log`模块就已经正式添加到项目中了。接下来就可以使用该组件提供的所有功能了，详细的使用说明可以查看：[接口文档](https://github.com/zifangsky/quick-modules/wiki)



## 参数配置详解 ##

`quick-log`模块在使用的时候提供了多个可以自定义的参数。详情的参数含义如下：

#### （1）enableCache： ####

用于设置是否启用日志缓存，默认为`false`。如果不启用日志缓存功能，则新产生的日志将会一条条地插入到数据库。



#### （2）setCacheManager： ####

用于设置日志缓存管理器，**只有上面的`enableCache`参数设置为`true`，设置该参数才会生效**。此外默认的日志缓存管理器是`cn.zifangsky.quickmodules.log.cache.impl.MemoryLogCacheManager`，采用内存缓存，当然也可以自行实现`cn.zifangsky.quickmodules.log.cache.LogCacheManager`接口，设置其他缓存存储方式。



#### （3）logManager： ####

用于自定义组件的`AbstractLogManager`的逻辑，目前包含以下几个方法：

- getUserInfo：获取用户基本信息。在这个组件中主要是用于在记录用户日志时，获取当前的操作用户。



#### （4）scheduledCron： ####

用于设置定时消费缓存的日志的`Cron`表达式，默认为`0 */2 * * * ?`，也就是每隔两分钟消费一次。



#### （5）selectByRolePrefixSql： ####

用于设置通过角色查询日志的SQL前缀，默认为`user_id IN(SELECT sys_user.id FROM sys_user,sys_user_role WHERE sys_user.id = sys_user_role.user_id AND sys_user_role.role_id = ?)`，也就是默认的**用户表**是`sys_user`，默认的**用户角色表**是`sys_user_role`

设置该参数主要是用于日志的分页查询接口使用。


#### （6）build： ####

该参数仅仅只是配置完毕的标识。