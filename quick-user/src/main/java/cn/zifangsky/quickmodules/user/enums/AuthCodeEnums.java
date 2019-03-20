package cn.zifangsky.quickmodules.user.enums;

/**
 * 登录/认证失败的错误信息
 *
 * @author zifangsky
 * @date 2017/1/3
 * @since 1.0.0
 */
public enum AuthCodeEnums {
    /**
     * 登录成功
     */
    LOGIN_SUCCESS(200, "登录成功！"),

    /**
     * 登录失败
     */
    LOGIN_FAILURE(401, "登录失败！"),

    /**
     * 未登录
     */
    UN_LOGIN(401, "您还未登录系统，无法访问该地址！"),
    /**
     * 被踢出
     */
    KICKOUT(401, "您的账号已在其他设备登录，若非本人操作，请立即重新登录并修改密码！"),
    /**
     * 没有权限
     */
    NO_PERMISSIONS(403, "您当前没有权限访问该地址！")
    ;

    AuthCodeEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 返回状态码
     */
    private int code;

    /**
     * 返回提示信息
     */
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
