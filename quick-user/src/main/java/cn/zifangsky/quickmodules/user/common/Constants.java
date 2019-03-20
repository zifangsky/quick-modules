package cn.zifangsky.quickmodules.user.common;

/**
 * 公共常量类
 *
 * @author zifangsky
 * @date 2017/11/1
 * @since 1.0.0
 */
public interface Constants {
    /**
     * from表单中的验证码的变量名
     */
    String FORM_VERIFY_CODE = "verify_code";
    /**
     * from表单中的用户名的变量名
     */
    String FORM_USERNAME = "username";
    /**
     * from表单中的密码的变量名
     */
    String FORM_PASSWORD = "password";
    /**
     * from表单中的登录类型的变量名
     */
    String FORM_LOGIN_TYPE = "type";
    /**
     * from表单中的手机号的变量名
     */
    String FORM_PHONE = "phone";
    /**
     * from表单中的手机验证码的变量名
     */
    String FORM_PHONE_CODE = "code";

    /**
     * 用户信息在session中存储的变量名
     */
    String SESSION_USER = "SESSION_USER";

    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 验证码在session中存储的变量名
     */
    String SESSION_VERIFY_CODE = "VERIFY_CODE";

    /**
     * Shiro被踢出的标识
     */
    String KICKED_OUT_FLAG = "KICKED_OUT";

    //Table names
    String TABLE_USER = "sys_user";
    String TABLE_ROLE = "sys_role";
    String TABLE_USER_ROLE = "sys_user_role";
    String TABLE_FUNC = "sys_function";
    String TABLE_ROLE_FUNC = "sys_role_function";

    /**
     * 资源ID的前缀
     */
    String FUNC_ID_PREFIX = "manage-";
    /**
     * 根节点
     */
    String FUNC_ID_ROOT =  FUNC_ID_PREFIX + "0";
}
