package cn.zifangsky.quickmodules.user.enums;

/**
 * 登录方式
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public enum LoginTypes {
    /**
     * 用户名+密码
     */
    Username_Password(1, "使用“用户名+密码”登录"),
    /**
     * 手机号码+验证码
     */
    Phone_Code(2, "使用“手机号码+验证码”登录")
    ;

    private Integer code;

    private String description;

    LoginTypes(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
