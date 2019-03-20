package cn.zifangsky.quickmodules.user.plugins;

import cn.zifangsky.quickmodules.common.enums.EncryptionTypes;
import cn.zifangsky.quickmodules.user.enums.ExpireTypes;
import cn.zifangsky.quickmodules.user.enums.FilterChainTypes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置基本信息
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public class WebUserInfo {

    /**
     * 密码加密方式
     */
    private EncryptionTypes encryptionType;

    /**
     * 短信验证码有效期
     */
    private Long phoneCodeExpire;
    /**
     * session有效期
     */
    private long globalSessionTimeout;

    /**
     * 登录URL
     */
    private String loginUrl;
    /**
     * 登录失败的URL
     */
    private String unauthorizedUrl;
    /**
     * 自定义的过滤Map
     */
    private Map<String, String> filterChainDefinitionMap;
    /**
     * 用于暴露出需要自定义的方法
     */
    private PluginManager pluginManager;
    /**
     * 登录时是否使用验证码
     */
    private Boolean loginVerifyCodeFlag;
    /**
     * 注册时是否使用验证码
     */
    private Boolean registerVerifyCodeFlag;
    /**
     * 是否删除当前登录用户的旧session
     */
    private Boolean deleteOldSession;
    /**
     * 是否是前后端分离架构
     */
    private Boolean separationArchitecture;

    /**
     * 图片验证码的宽度
     */
    private Integer verifyImageWidth;

    /**
     * 图片验证码的高度
     */
    private Integer verifyImageHeight;

    /**
     * shiro aop注解匹配的表达式
     */
    private String aopExpression;

    /**
     * 登录校验URL
     */
    private String loginCheckUrl;
    /**
     * 生成验证码图片的URL
     */
    private String authImageUrl;
    /**
     * 生成登录时手机验证码的URL
     */
    private String loginPhoneVerifyCodeUrl;
    /**
     * 退出登录的URL
     */
    private String logoutUrl;

    WebUserInfo(WebUserInfo.Builder builder){
        this.encryptionType = builder.encryptionType;
        this.phoneCodeExpire = builder.phoneCodeExpire;
        this.globalSessionTimeout = builder.globalSessionTimeout;
        this.loginUrl = builder.loginUrl;
        this.unauthorizedUrl = builder.unauthorizedUrl;
        this.pluginManager = builder.pluginManager;
        this.loginVerifyCodeFlag = builder.loginVerifyCodeFlag;
        this.registerVerifyCodeFlag = builder.registerVerifyCodeFlag;
        this.deleteOldSession = builder.deleteOldSession;
        this.separationArchitecture = builder.separationArchitecture;
        this.verifyImageHeight = builder.verifyImageHeight;
        this.verifyImageWidth = builder.verifyImageWidth;
        this.aopExpression = builder.aopExpression;
        this.loginCheckUrl = builder.loginCheckUrl;
        this.authImageUrl = builder.authImageUrl;
        this.loginPhoneVerifyCodeUrl = builder.loginPhoneVerifyCodeUrl;
        this.logoutUrl = builder.logoutUrl;

        //过滤URL
        this.filterChainDefinitionMap = builder.filterChainMap;
    }

    public EncryptionTypes getEncryptionType() {
        return encryptionType;
    }

    public Long getPhoneCodeExpire() {
        return phoneCodeExpire;
    }

    public long getGlobalSessionTimeout() {
        return globalSessionTimeout;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public Boolean getLoginVerifyCodeFlag() {
        return loginVerifyCodeFlag;
    }

    public Boolean getRegisterVerifyCodeFlag() {
        return registerVerifyCodeFlag;
    }

    public Boolean getDeleteOldSession() {
        return deleteOldSession;
    }

    public Boolean getSeparationArchitecture() {
        return separationArchitecture;
    }

    public Integer getVerifyImageWidth() {
        return verifyImageWidth;
    }

    public Integer getVerifyImageHeight() {
        return verifyImageHeight;
    }

    public String getAopExpression() {
        return aopExpression;
    }

    public String getLoginCheckUrl() {
        return loginCheckUrl;
    }

    public String getAuthImageUrl() {
        return authImageUrl;
    }

    public String getLoginPhoneVerifyCodeUrl() {
        return loginPhoneVerifyCodeUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    /**
     * 构建类
     */
    public static class Builder {
        /**
         * 密码加密方式
         */
        private EncryptionTypes encryptionType = EncryptionTypes.Sha256Crypt;

        /**
         * 短信验证码有效期
         */
        private Long phoneCodeExpire = ExpireTypes.DEFAULT_PHONE_CODE.getTime();
        /**
         * session有效期
         */
        private long globalSessionTimeout = ExpireTypes.SHIRO_SESSION.getTime();

        /**
         * 登录URL
         */
        private String loginUrl = "/login.html";
        /**
         * 登录失败的URL
         */
        private String unauthorizedUrl = "/error.html";
        /**
         * 自定义的过滤Map
         */
        private Map<String, String> filterChainMap;
        /**
         * 用于暴露出需要自定义的方法
         */
        private PluginManager pluginManager;
        /**
         * 登录时是否使用验证码
         */
        private Boolean loginVerifyCodeFlag = false;
        /**
         * 注册时是否使用验证码
         */
        private Boolean registerVerifyCodeFlag = false;
        /**
         * 是否删除当前登录用户的旧session
         */
        private Boolean deleteOldSession = false;
        /**
         * 是否是前后端分离架构
         */
        private Boolean separationArchitecture = false;

        /**
         * 图片验证码的宽度
         */
        private Integer verifyImageWidth = 140;

        /**
         * 图片验证码的高度
         */
        private Integer verifyImageHeight = 40;

        /**
         * shiro aop注解匹配的表达式
         */
        private String aopExpression;

        /**
         * 登录校验URL
         */
        private String loginCheckUrl = "/check";
        /**
         * 生成验证码图片的URL
         */
        private String authImageUrl = "/generateAuthImage";
        /**
         * 生成登录时手机验证码的URL
         */
        private String loginPhoneVerifyCodeUrl = "/sendLoginPhoneVerifyCode";
        /**
         * 退出登录的URL
         */
        private String logoutUrl = "/logout";

        /**
         * 密码加密方式
         * <p>默认：EncryptionTypes.Sha256Crypt</p>
         */
        public Builder encryptionType(EncryptionTypes encryptionType){
            this.encryptionType = encryptionType;
            return this;
        }

        /**
         * 短信验证码有效期
         * <p>默认：60秒</p>
         */
        public Builder phoneCodeExpire(Long phoneCodeExpire) {
            this.phoneCodeExpire = phoneCodeExpire;
            return this;
        }

        /**
         * session有效期
         * <p>默认：2小时</p>
         */
        public Builder globalSessionTimeout(Long globalSessionTimeout) {
            this.globalSessionTimeout = globalSessionTimeout;
            return this;
        }

        /**
         * 用于设置一些自定义校验逻辑，根据需求复写其中方法
         */
        public Builder pluginManager(PluginManager pluginManager) {
            this.pluginManager = pluginManager;
            return this;
        }

        /**
         * 登录时是否使用验证码
         * <p>默认：false</p>
         */
        public Builder enableLoginVerifyCode(Boolean loginVerifyCodeFlag){
            this.loginVerifyCodeFlag = loginVerifyCodeFlag;
            return this;
        }

        /**
         * 注册时是否使用验证码
         * <p>默认：false</p>
         */
        public Builder enableRegisterVerifyCode(Boolean registerVerifyCodeFlag){
            this.registerVerifyCodeFlag = registerVerifyCodeFlag;
            return this;
        }

        /**
         * 同一个用户只允许在一个设备登录
         * <p>默认：false</p>
         */
        public Builder deleteOldSession(Boolean deleteOldSession){
            this.deleteOldSession = deleteOldSession;
            return this;
        }

        /**
         * 设置是否是前后端分离架构
         * <p>默认：false</p>
         */
        public Builder separationArchitecture(Boolean separationArchitecture){
            this.separationArchitecture = separationArchitecture;
            return this;
        }

        /**
         * 图片验证码的宽度
         * <p>默认：140</p>
         */
        public Builder setVerifyImageWidth(Integer verifyImageWidth) {
            this.verifyImageWidth = verifyImageWidth;
            return this;
        }

        /**
         * 图片验证码的高度
         * <p>默认：40</p>
         */
        public Builder setVerifyImageHeight(Integer verifyImageHeight) {
            this.verifyImageHeight = verifyImageHeight;
            return this;
        }

        /**
         * shiro aop注解匹配的表达式，比如：<b>execution(* cn.zifangsky..controller..*.*(..))</b>
         * <p>Note：如果不设置则不支持Shiro注解</p>
         */
        public Builder aopExpression(String aopExpression) {
            this.aopExpression = aopExpression;
            return this;
        }

        /**
         * 登录URL
         * <p>默认：/login.html</p>
         */
        public Builder loginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
            return this;
        }

        /**
         * 登录校验URL
         * <p>默认：/check</p>
         */
        public Builder loginCheckUrl(String loginCheckUrl) {
            this.loginCheckUrl = loginCheckUrl;
            return this;
        }

        /**
         * 登录失败的URL
         * <p>默认：/error.html</p>
         */
        public Builder unauthorizedUrl(String unauthorizedUrl) {
            this.unauthorizedUrl = unauthorizedUrl;
            return this;
        }

        /**
         * 生成验证码图片的URL
         * <p>默认：/generateAuthImage</p>
         */
        public Builder authImageUrl(String authImageUrl) {
            this.authImageUrl = authImageUrl;
            return this;
        }

        /**
         * 生成登录时手机验证码的URL
         * <p>默认：/sendLoginPhoneVerifyCode</p>
         */
        public Builder loginPhoneVerifyCodeUrl(String loginPhoneVerifyCodeUrl) {
            this.loginPhoneVerifyCodeUrl = loginPhoneVerifyCodeUrl;
            return this;
        }

        /**
         * 退出登录的URL
         * <p>默认：/logout</p>
         */
        public Builder logoutUrl(String logoutUrl) {
            this.logoutUrl = logoutUrl;
            return this;
        }

        /**
         * 设置自定义的过滤Map
         */
        public Builder filterChainMap(Map<String, String> filterMap){
            this.filterChainMap = new LinkedHashMap<>();

            //1. 添加几个默认的过滤信息
            filterChainMap.put(this.loginUrl, FilterChainTypes.Anon.getCode());
            filterChainMap.put(this.loginCheckUrl, FilterChainTypes.Anon.getCode());
            filterChainMap.put(this.authImageUrl, FilterChainTypes.Anon.getCode());
            filterChainMap.put(this.loginPhoneVerifyCodeUrl, FilterChainTypes.Anon.getCode());
            filterChainMap.put(this.unauthorizedUrl, FilterChainTypes.Anon.getCode());

            //2. 添加自定义的过滤信息
            if(filterMap != null){
                filterMap.forEach((key, value) -> filterChainMap.put(key, value));
            }

            return this;
        }

        public WebUserInfo build(){
            //如果没有自定义的PluginManager，则new一个
            if(this.pluginManager == null){
                this.pluginManager = new PluginManager();
            }

            return new WebUserInfo(this);
        }

    }

}
