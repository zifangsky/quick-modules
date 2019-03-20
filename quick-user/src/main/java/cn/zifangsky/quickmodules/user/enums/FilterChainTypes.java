package cn.zifangsky.quickmodules.user.enums;

/**
 * Shiro的过滤类型
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public enum FilterChainTypes {
    /**
     * 不用登陆
     */
    Anon("anon"),
    /**
     * 需要登录
     */
    Authc("authc"),
    /**
     * 删除当前登录用户的旧session
     */
    Kickout("kickout")
    ;

    private String code;

    FilterChainTypes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
