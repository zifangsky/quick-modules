package cn.zifangsky.quickmodules.user.config;

import cn.zifangsky.quickmodules.common.annotations.ConditionalOnBeanUndefined;
import cn.zifangsky.quickmodules.user.enums.FilterChainTypes;
import cn.zifangsky.quickmodules.user.plugins.WebUserInfo;
import cn.zifangsky.quickmodules.user.shiro.AjaxFormAuthenticationFilter;
import cn.zifangsky.quickmodules.user.shiro.AjaxKickoutAccessControlFilter;
import cn.zifangsky.quickmodules.user.shiro.CustomRealm;
import cn.zifangsky.quickmodules.user.shiro.KickoutAccessControlFilter;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 集成Shiro权限管理
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
@Configuration
@EnableAspectJAutoProxy
public class ShiroConfig {

    @Bean
    public EhCacheManager ehCacheManager(CacheManager ehCacheCacheManager){
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManager(ehCacheCacheManager);

        return cacheManager;
    }

    /**
     * 自定义{@link Realm}
     */
    @Bean
    @ConditionalOnBeanUndefined(CustomRealm.class)
    public CustomRealm customRealm(){
        return new CustomRealm();
    }

    /**
     * 自定义Shiro的{@link Cookie}信息
     */
    @Bean
    @ConditionalOnBeanUndefined(SimpleCookie.class)
    public SimpleCookie sessionIdCookie(){
        SimpleCookie cookie = new SimpleCookie("user_session_id");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);

        return cookie;
    }

    /**
     * 自定义{@link SessionDAO}
     * <p>1. 使用缓存存储{@link org.apache.shiro.session.Session}</p>
     * <p>2. 使用Java Uuid生成SessionId</p>
     */
    @Bean
    @ConditionalOnBeanUndefined(AbstractSessionDAO.class)
    public AbstractSessionDAO sessionDAO(EhCacheManager ehCacheManager){
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setCacheManager(ehCacheManager);
        sessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());

        return sessionDAO;
    }

    /**
     * 自定义{@link SessionManager}
     */
    @Bean
    @ConditionalOnBeanUndefined(DefaultWebSessionManager.class)
    public DefaultWebSessionManager sessionManager(Cookie cookie, SessionDAO sessionDAO, WebUserInfo webUserInfo){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置cookie参数
        sessionManager.setSessionIdCookie(cookie);
        //设置session的存储，以及SessionId的生成方式
        sessionManager.setSessionDAO(sessionDAO);
        //设置过期时间（默认2小时）
        sessionManager.setGlobalSessionTimeout(webUserInfo.getGlobalSessionTimeout());

        return sessionManager;
    }

    /**
     * 自定义{@link SecurityManager}
     */
    @Bean
    @ConditionalOnBeanUndefined(DefaultWebSecurityManager.class)
    public SecurityManager securityManager(CustomRealm customRealm
            , SessionManager sessionManager, EhCacheManager ehCacheManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(customRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(ehCacheManager);

        return securityManager;
    }

    @Bean("lifecycleBeanPostProcessor")
    @ConditionalOnBeanUndefined(LifecycleBeanPostProcessor.class)
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 添加shiro aop注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    @ConditionalOnBeanUndefined(AuthorizationAttributeSourceAdvisor.class)
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager, WebUserInfo webUserInfo){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();

        if(webUserInfo.getAopExpression() != null){
            //定义匹配表达式，只拦截controller中的方法
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(webUserInfo.getAopExpression());
            authorizationAttributeSourceAdvisor.setClassFilter(pointcut);
        }

        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 自定义{@link ShiroFilterFactoryBean}
     */
    @Bean
    @ConditionalOnBeanUndefined(ShiroFilterFactoryBean.class)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, WebUserInfo webUserInfo){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl(webUserInfo.getLoginUrl());
        factoryBean.setUnauthorizedUrl(webUserInfo.getUnauthorizedUrl());

        Map<String,Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(FilterChainTypes.Kickout.getCode(), new KickoutAccessControlFilter());
        //如果是前后端分离架构，则重写登录过滤器
        if(webUserInfo.getSeparationArchitecture()){
            filterMap.put(FilterChainTypes.Kickout.getCode(), new AjaxKickoutAccessControlFilter());
            filterMap.put(FilterChainTypes.Authc.getCode(), new AjaxFormAuthenticationFilter());
        }

        //添加自定义的过滤器
        factoryBean.setFilters(filterMap);
        //设置过滤链
        factoryBean.setFilterChainDefinitionMap(webUserInfo.getFilterChainDefinitionMap());

        return factoryBean;
    }

}
