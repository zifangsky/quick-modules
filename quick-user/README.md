## 模块介绍 ##

此模块是基于 [easylimit](https://github.com/zifangsky/easylimit) 框架封装的权限控制模块，包括`easylimit`相关的所有功能以及用户、角色、资源等基础增删改查的`RESTful API`。



## 开始使用 ##

#### （1）引入依赖： ####

```xml
<dependency>
	<groupId>cn.zifangsky.quickmodules</groupId>
	<artifactId>quick-user</artifactId>
	<version>1.1.0-RELEASE</version>
</dependency>
```



#### （2）在数据库中新建如下的表： ####

```sql
-- ----------------------------
-- Table structure for sys_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_function`;
CREATE TABLE `sys_function` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `myself_id` varchar(255) NOT NULL COMMENT '自身唯一标识ID',
  `parent_id` varchar(255) DEFAULT NULL COMMENT '父级资源项ID ,取myselfid值',
  `name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `type` int(11) DEFAULT NULL COMMENT '资源类型： 1.菜单   2.按钮',
  `level` int(11) DEFAULT NULL COMMENT '层级：1.一级导航菜单  2.二级导航菜单  3.功能按钮',
  `path_url` varchar(255) DEFAULT NULL COMMENT '权限路径',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '图标路径',
  `sequence_num` int(11) DEFAULT NULL COMMENT '排序',
  `state` int(11) DEFAULT '0' COMMENT '状态（0:正常；1：删除）',
  `description` varchar(255) DEFAULT NULL COMMENT '资源项描述',
  `institution_id` varchar(50) DEFAULT NULL COMMENT '公司id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `myself_id` (`myself_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='资源/权限表';

-- ----------------------------
-- Records of sys_function
-- ----------------------------
INSERT INTO `sys_function` VALUES ('1', 'manage-0', '0', '所有权限', '1', '0', '', '', '0', '0', '所有权限', '', '2017-06-28 18:53:53', 'wxn');
INSERT INTO `sys_function` VALUES ('2', 'manage-10', 'manage-0', '测试权限1', '1', '1', '/aaa/bbb', '', '1', '0', '测试权限1', '', '2019-03-20 00:00:03', '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` int(4) DEFAULT '0' COMMENT '状态（0:正常；1：删除）',
  `institution_id` int(11) DEFAULT NULL COMMENT '公司id',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'manager', '超级管理员', '0', null, 'admin', '2017-07-11 13:53:13');
INSERT INTO `sys_role` VALUES ('2', '测试角色', '1', '0', null, null, null);

-- ----------------------------
-- Table structure for sys_role_function
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_function`;
CREATE TABLE `sys_role_function` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint(19) NOT NULL COMMENT '角色id',
  `func_id` bigint(19) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色资源表';

-- ----------------------------
-- Records of sys_role_function
-- ----------------------------
INSERT INTO `sys_role_function` VALUES ('1', '1', '1');
INSERT INTO `sys_role_function` VALUES ('2', '2', '2');
INSERT INTO `sys_role_function` VALUES ('3', '1', '2');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(255) NOT NULL COMMENT '登录名',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `encrypt_mode` varchar(100) DEFAULT NULL COMMENT '密码加密方式',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `user_type` int(2) DEFAULT '0' COMMENT '0：后台用户；1：前台用户',
  `institution_id` int(11) DEFAULT NULL COMMENT '机构id',
  `status` int(4) DEFAULT '0' COMMENT '用户状态（0:可用；1:锁定；2:未开通）',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '删除状态：0正常；1删除',
  `login_ip` varchar(100) DEFAULT NULL,
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上次登录时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '超级管理员', '$5$a8lsN8nw$oTwkzm3kNLqfkoZsfvL6RFu51DfxEtNdWJBvqwYD1P5', 'Sha256Crypt', '12345678909', '', '1', null, '0', '0', '127.0.0.1', '2019-03-21 11:55:47', null, '2017-08-10 00:00:01');
INSERT INTO `sys_user` VALUES ('2', 'zifangsky', 'zifangsky', '$5$PxwFFmfM$U0XkUfrCqEYKmFAzPGb8hJt.7fqXmOHvp1gHeMuo4m5', 'Sha256Crypt', '15812345678', 'admin@zifangsky.cn', '1', null, '0', '0', '127.0.0.1', '2019-03-18 16:31:42', null, '2017-08-11 00:00:01');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(19) NOT NULL COMMENT '用户id',
  `role_id` bigint(19) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2', '2');
```


#### （3）在项目中添加相应配置： ####

```java
package cn.zifangsky.quickmodules.example.config;

import cn.zifangsky.easylimit.enums.EncryptionTypeEnums;
import cn.zifangsky.easylimit.filter.impl.support.DefaultFilterEnums;
import cn.zifangsky.quickmodules.user.annotations.EnableWebUser;
import cn.zifangsky.quickmodules.user.plugins.WebTokenUserInfo;
import cn.zifangsky.quickmodules.user.plugins.WebUserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * 测试使用quick-user模块
 * @author zifangsky
 * @date 2020/12/7
 * @since 1.1.0
 */
@Configuration
@EnableWebUser
public class QuickUserModuleConfig {

    /**
     * 前后端分离开发模式
     */
    @Bean
    public WebTokenUserInfo webTokenUserInfo(){
        //添加指定路径的权限校验
        LinkedHashMap<String, String[]> patternPathFilterMap = this.patternPathFilterMap();

        WebTokenUserInfo userInfo = (WebTokenUserInfo) new WebTokenUserInfo()
                .encryptionType(EncryptionTypeEnums.Sha256Crypt)
                .loginCheckUrl("/check")
                .unauthorizedUrl("/error/403.html")
                .logoutUrl("/logout")
                //启用登录校验验证码
                .enableLoginVerifyCode(false)
                //同一个用户只允许在一个设备登录
                .kickOutOldSessions(true)
                //开启权限注解匹配的切面表达式
                .aopExpression("execution(* cn.zifangsky..controller..*.*(..))");

        return userInfo
                .filter()
                .filterChainMap(patternPathFilterMap)
                .build();
    }

    /**
     * 传统MVC开发模式
     */
//    @Bean
    public WebUserInfo webUserInfo(){
        //添加指定路径的权限校验
        LinkedHashMap<String, String[]> patternPathFilterMap = this.patternPathFilterMap();

        WebUserInfo userInfo = new WebUserInfo()
                .encryptionType(EncryptionTypeEnums.Sha256Crypt)
                .loginCheckUrl("/check")
                .unauthorizedUrl("/error/403.html")
                .logoutUrl("/logout")
                //启用登录校验验证码
                .enableLoginVerifyCode(false)
                //同一个用户只允许在一个设备登录
                .kickOutOldSessions(true)
                //开启权限注解匹配的切面表达式
                .aopExpression("execution(* cn.zifangsky..controller..*.*(..))");

        return userInfo
                .filter()
                .filterChainMap(patternPathFilterMap)
                .build();
    }


    /**
     * 请求路径与需要的访问权限
     */
    private LinkedHashMap<String, String[]> patternPathFilterMap(){
        LinkedHashMap<String, String[]> patternPathFilterMap = new LinkedHashMap<>();
        patternPathFilterMap.put("/css/**", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        patternPathFilterMap.put("/layui/**", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        patternPathFilterMap.put("/test/greeting", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
//        patternPathFilterMap.put("/test/selectByUsername", new String[]{"perms[/aaa/bbb]"});

        //其他路径需要登录才能访问
        patternPathFilterMap.put("/**", new String[]{DefaultFilterEnums.LOGIN.getFilterName()});

        return patternPathFilterMap;
    }
}
```

#### （4）正式使用： ####

在添加上述配置后，`quick-user`模块就已经正式添加到项目中了。接下来就可以使用该组件提供的所有接口和权限控制了。详细的使用说明可以查看：[接口文档](https://github.com/zifangsky/quick-modules/wiki)



## 参数配置详解 ##

`quick-user`模块在使用的时候提供了多个可以自定义的参数。详情的参数含义如下：

#### （1）encryptionType： ####

用于设置数据库中密码的加密方式，默认的密码加密方式是`EncryptionTypes.Sha256Crypt`。目前可供选择的加密方式有以下几种：

- Base64
- Md5
- Sha1
- Sha256
- Sha512
- Md5Crypt
- Sha256Crypt
- Sha512Crypt



#### （2）phoneCodeExpire： ####

用于设置短信验证码的有效期，默认的有效期是`60秒`。



#### （3）globalSessionTimeout： ####

用于设置用户会话的有效期，默认的有效期是`2小时`。



#### （4）pluginManager： ####

用于自定义组件的`PluginManager`的逻辑，目前包含以下几个方法：

- `checkCustomUsernamePasswordValidatedInfo`：“用户名+密码”模式，校验自定义的密码加密方式（使用的时候需要自行实现）
- `sendLoginPhoneVerifyCode`：发送登录时的短信验证码（使用的时候需要自行实现）
- `sendRegisterPhoneVerifyCode`：发送注册时的短信验证码（使用的时候需要自行实现）
- `checkPhoneVerifyCode`：校验指定手机号的验证码（使用的时候需要自行实现）
- `generateAuthImage`：生成图片验证码的逻辑
- `checkVerifyCode`：校验图片验证码的逻辑



#### （5）enableLoginVerifyCode： ####

用于设置登录时是否使用验证码，默认为`false`。**如果设置为`true`，那么登录时将会校验图片验证码，具体的校验逻辑由上面的`PluginManager`的`checkVerifyCode`方法决定**。



#### （6）enableRegisterVerifyCode： ####

用于设置注册时是否使用验证码，默认为`false`。具体使用方法跟上面类似。



#### （7）kickOutOldSessions： ####

用于设置是否允许同一个用户同时在多个设备登录，默认为`false`。**如果设置为`true`，那么某个用户在新设备登录时，其在旧设备的登录状态将会被踢出，而且会根据当前不同请求方式返回不同的提示信息**，具体如下：

i）如果当前是`Ajax`请求，则返回提示：

```json
{ 
    "code": 401,
    "msg": "您的账号已在其他设备登录，若非本人操作，请立即重新登录并修改密码！"
}
```

ii）如果是其他请求方式，则返回提示：

重定向到登录页面，并带上`kickout=1`参数。



附：如果是`Ajax`请求某个接口，而且没有权限访问，则会返回如下提示：

```json
{ 
    "code": 403,
    "msg": "您当前没有权限访问该地址！"
}
```



#### （8）aopExpression： ####

此方法用于设置开启`easylimit`的AOP注解需要的表达式，比如：`execution(* cn.zifangsky..controller..*.*(..))`。



#### （9）verifyImageWidth： ####

用于设置图片验证码的宽度，默认为`140`。



#### （10）verifyImageHeight： ####

用于设置图片验证码的高度，默认为`40`。



#### （11）loginUrl： ####

用于自定义登录的URL，默认为`/login.html`。



#### （12）loginCheckUrl： ####

用于自定义登录校验的URL，默认为`/check`。



#### （13）unauthorizedUrl： ####

用于自定义登录失败的URL，默认为`/error.html`。



#### （14）authImageUrl： ####

用于自定义生成验证码图片的URL，默认为`/generateAuthImage`。



#### （15）loginPhoneVerifyCodeUrl： ####

用于自定义生成登录时手机验证码的URL，默认为`/sendLoginPhoneVerifyCode`。



#### （16）logoutUrl： ####

用于自定义退出登录的URL，默认为`/logout`。



#### （17）filterChainMap： ####

用于添加基于`easylimit`的URL过滤集合，需要传递的参数是一个`LinkedHashMap`，比如：

```java
LinkedHashMap<String, String[]> patternPathFilterMap = new LinkedHashMap<>();
patternPathFilterMap.put("/css/**", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
patternPathFilterMap.put("/layui/**", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
patternPathFilterMap.put("/test/greeting", new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
//patternPathFilterMap.put("/test/selectByUsername", new String[]{"perms[/aaa/bbb]"});

//其他路径需要登录才能访问
patternPathFilterMap.put("/**", new String[]{DefaultFilterEnums.LOGIN.getFilterName()});
```

其中这里的`DefaultFilterEnums`内置了五种最常用的过滤类型：

- `ANONYMOUS`：不用登录即可随意访问，一般用于设置静态资源不用登录就可以访问。**需要注意的是，这类URL需要在最前面设置**。
- `LOGIN`：设置哪些URL需要登录后才允许访问。
- `LOGOUT`：注销的URL
- `ROLES`：设置某个URL需要何种角色才能访问，比如：roles[admin]
- `PERMS`：设置某个URL需要何种权限才能访问，比如：perms[/aaa/bbb]



#### （18）build： ####

该参数仅仅只是配置完毕的标识。