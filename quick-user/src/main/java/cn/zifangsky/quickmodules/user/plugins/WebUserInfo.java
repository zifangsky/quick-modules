package cn.zifangsky.quickmodules.user.plugins;

import cn.zifangsky.easylimit.enums.EncryptionTypeEnums;
import cn.zifangsky.quickmodules.user.enums.ExpireTypes;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置基本信息
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
@Getter
public class WebUserInfo {

    /**
     * 密码加密方式
     */
    private EncryptionTypeEnums encryptionType = EncryptionTypeEnums.Sha256Crypt;

    /**
     * 短信验证码有效期
     */
    private Long phoneCodeExpire = ExpireTypes.DEFAULT_PHONE_CODE.getTime();
    /**
     * session有效期
     */
    private long globalSessionTimeout = ExpireTypes.USER_SESSION.getTime();

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
    protected LinkedHashMap<String, String[]> filterChainDefinitionMap;
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
    private Boolean kickOutOldSessions = false;

    /**
     * AOP权限注解匹配的表达式
     */
    private String aopExpression;

    /**
     * 图片验证码的宽度
     */
    private Integer verifyImageWidth = 140;

    /**
     * 图片验证码的高度
     */
    private Integer verifyImageHeight = 40;

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
    public WebUserInfo encryptionType(EncryptionTypeEnums encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    /**
     * 短信验证码有效期
     * <p>默认：60秒</p>
     */
    public WebUserInfo phoneCodeExpire(Long phoneCodeExpire) {
        this.phoneCodeExpire = phoneCodeExpire;
        return this;
    }

    /**
     * session有效期
     * <p>默认：2小时</p>
     */
    public WebUserInfo globalSessionTimeout(Long globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
        return this;
    }

    /**
     * 用于设置一些自定义校验逻辑，根据需求复写其中方法
     */
    public WebUserInfo pluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        return this;
    }

    /**
     * 登录时是否使用验证码
     * <p>默认：false</p>
     */
    public WebUserInfo enableLoginVerifyCode(Boolean loginVerifyCodeFlag) {
        this.loginVerifyCodeFlag = loginVerifyCodeFlag;
        return this;
    }

    /**
     * 注册时是否使用验证码
     * <p>默认：false</p>
     */
    public WebUserInfo enableRegisterVerifyCode(Boolean registerVerifyCodeFlag) {
        this.registerVerifyCodeFlag = registerVerifyCodeFlag;
        return this;
    }

    /**
     * 同一个用户只允许在一个设备登录
     * <p>默认：false</p>
     */
    public WebUserInfo kickOutOldSessions(Boolean kickOutOldSessions) {
        this.kickOutOldSessions = kickOutOldSessions;
        return this;
    }

    /**
     * AOP权限注解匹配的表达式，比如：<b>execution(* cn.zifangsky..controller..*.*(..))</b>
     */
    public WebUserInfo aopExpression(String aopExpression) {
        this.aopExpression = aopExpression;
        return this;
    }

    /**
     * 图片验证码的宽度
     * <p>默认：140</p>
     */
    public WebUserInfo verifyImageWidth(Integer verifyImageWidth) {
        this.verifyImageWidth = verifyImageWidth;
        return this;
    }

    /**
     * 图片验证码的高度
     * <p>默认：40</p>
     */
    public WebUserInfo verifyImageHeight(Integer verifyImageHeight) {
        this.verifyImageHeight = verifyImageHeight;
        return this;
    }

    /**
     * 登录URL
     * <p>默认：/login.html</p>
     */
    public WebUserInfo loginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    /**
     * 登录校验URL
     * <p>默认：/check</p>
     */
    public WebUserInfo loginCheckUrl(String loginCheckUrl) {
        this.loginCheckUrl = loginCheckUrl;
        return this;
    }

    /**
     * 登录失败的URL
     * <p>默认：/error.html</p>
     */
    public WebUserInfo unauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
        return this;
    }

    /**
     * 生成验证码图片的URL
     * <p>默认：/generateAuthImage</p>
     */
    public WebUserInfo authImageUrl(String authImageUrl) {
        this.authImageUrl = authImageUrl;
        return this;
    }

    /**
     * 生成登录时手机验证码的URL
     * <p>默认：/sendLoginPhoneVerifyCode</p>
     */
    public WebUserInfo loginPhoneVerifyCodeUrl(String loginPhoneVerifyCodeUrl) {
        this.loginPhoneVerifyCodeUrl = loginPhoneVerifyCodeUrl;
        return this;
    }

    /**
     * 退出登录的URL
     * <p>默认：/logout</p>
     */
    public WebUserInfo logoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
        return this;
    }

    /**
     * 开始设置需要过滤的URL
     */
    public FilterChainBuilder filter() {
        return new FilterChainBuilder(this);
    }

    /**
     * 设置自定义的过滤Map
     */
    public WebUserInfo filterChainMap(FilterChain filterChain) {
        this.filterChainDefinitionMap = new LinkedHashMap<>();
        LinkedHashMap<String, String[]> filterChainMap = filterChain.getFilterChainMap();

        if (filterChainMap.size() > 0) {
            for (Map.Entry<String, String[]> entry : filterChainMap.entrySet()) {
                this.filterChainDefinitionMap.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }
}
