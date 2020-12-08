package cn.zifangsky.quickmodules.user.config;

import cn.zifangsky.easylimit.DefaultWebSecurityManager;
import cn.zifangsky.easylimit.SecurityManager;
import cn.zifangsky.easylimit.TokenWebSecurityManager;
import cn.zifangsky.easylimit.cache.Cache;
import cn.zifangsky.easylimit.cache.impl.DefaultMemoryCache;
import cn.zifangsky.easylimit.enums.ProjectModeEnums;
import cn.zifangsky.easylimit.filter.impl.support.FilterRegistrationFactoryBean;
import cn.zifangsky.easylimit.permission.aop.PermissionsAnnotationAdvisor;
import cn.zifangsky.easylimit.realm.Realm;
import cn.zifangsky.easylimit.session.SessionDAO;
import cn.zifangsky.easylimit.session.SessionIdFactory;
import cn.zifangsky.easylimit.session.TokenDAO;
import cn.zifangsky.easylimit.session.impl.AbstractWebSessionManager;
import cn.zifangsky.easylimit.session.impl.DefaultTokenOperateResolver;
import cn.zifangsky.easylimit.session.impl.MemorySessionDAO;
import cn.zifangsky.easylimit.session.impl.support.CookieWebSessionManager;
import cn.zifangsky.easylimit.session.impl.support.DefaultCacheTokenDAO;
import cn.zifangsky.easylimit.session.impl.support.RandomCharacterSessionIdFactory;
import cn.zifangsky.easylimit.session.impl.support.TokenWebSessionManager;
import cn.zifangsky.quickmodules.user.easylimit.CustomRealm;
import cn.zifangsky.quickmodules.user.plugins.WebTokenUserInfo;
import cn.zifangsky.quickmodules.user.plugins.WebUserInfo;
import cn.zifangsky.quickmodules.user.service.UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.concurrent.TimeUnit;

/**
 * EasyLimit框架的配置
 *
 * @author zifangsky
 * @date 2020/1/3
 * @since 1.0.0
 */
@Configuration
public class EasyLimitConfig {

    /**
     * 配置缓存
     */
    @Bean
    public Cache cache() {
        return new DefaultMemoryCache();
    }

    /**
     * 配置Realm
     */
    @Bean
    public Realm realm(UserService userService, WebUserInfo webUserInfo, Cache cache) {
        CustomRealm realm = new CustomRealm(userService, webUserInfo);
        //缓存角色、权限信息
        realm.setEnablePermissionInfoCache(true);
        realm.setPermissionInfoCache(cache);

        return realm;
    }

    /**
     * 配置Session的存储方式
     */
    @Bean
    public SessionDAO sessionDAO(Cache cache) {
        return new MemorySessionDAO();
    }

    /**
     * 配置Token的存储方式
     */
    @Bean
    public TokenDAO tokenDAO(Cache cache) {
        return new DefaultCacheTokenDAO(cache);
    }

    /**
     * 配置session管理器
     */
    @Bean
    public AbstractWebSessionManager sessionManager(SessionDAO sessionDAO, TokenDAO tokenDAO, WebUserInfo webUserInfo) {
        AbstractWebSessionManager sessionManager = null;

        if (webUserInfo instanceof WebTokenUserInfo) {
            //创建基于Token的session管理器
            sessionManager = new TokenWebSessionManager(((WebTokenUserInfo) webUserInfo).getTokenInfo(), new DefaultTokenOperateResolver(), tokenDAO);
        } else {
            sessionManager = new CookieWebSessionManager();
        }

        sessionManager.setSessionDAO(sessionDAO);

        //设置定时校验的时间为2分钟
        sessionManager.setSessionValidationInterval(2L);
        sessionManager.setSessionValidationUnit(TimeUnit.MINUTES);

        //设置sessionId的生成方式
        SessionIdFactory sessionIdFactory = new RandomCharacterSessionIdFactory();
        sessionManager.setSessionIdFactory(sessionIdFactory);

        return sessionManager;
    }

    /**
     * 认证、权限、session等管理的入口
     * @return
     */
    @Bean
    public SecurityManager securityManager(Realm realm, AbstractWebSessionManager sessionManager, WebUserInfo webUserInfo) {
        DefaultWebSecurityManager securityManager = null;

        if (webUserInfo instanceof WebTokenUserInfo) {
            securityManager = new TokenWebSecurityManager(realm, (TokenWebSessionManager) sessionManager);
        } else {
            securityManager = new DefaultWebSecurityManager(realm, sessionManager);
        }

        //是否删除当前登录用户的旧session
        securityManager.setKickOutOldSessions(webUserInfo.getKickOutOldSessions());

        return securityManager;
    }

    /**
     * 将filter添加到Spring管理
     */
    @Bean
    public FilterRegistrationFactoryBean filterRegistrationFactoryBean(SecurityManager securityManager, WebUserInfo webUserInfo) {
        FilterRegistrationFactoryBean factoryBean = new FilterRegistrationFactoryBean(ProjectModeEnums.TOKEN,
                securityManager,
                webUserInfo.getFilterChainDefinitionMap());

        //设置几个登录、未授权等相关URL
        factoryBean.setLoginUrl(webUserInfo.getLoginUrl());
        factoryBean.setLoginCheckUrl(webUserInfo.getLoginCheckUrl());
        factoryBean.setUnauthorizedUrl(webUserInfo.getUnauthorizedUrl());
        factoryBean.setLogoutRedirectUrl(webUserInfo.getLoginUrl());

        return factoryBean;
    }

    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("filterRegistrationFactoryBean");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * 添加对权限注解的支持
     */
    @Bean
    public PermissionsAnnotationAdvisor permissionsAnnotationAdvisor(WebUserInfo webUserInfo) {
        return new PermissionsAnnotationAdvisor(webUserInfo.getAopExpression());
    }

}
