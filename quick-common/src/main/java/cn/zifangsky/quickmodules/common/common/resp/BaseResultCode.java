package cn.zifangsky.quickmodules.common.common.resp;

/**
 * API接口返回状态码
 *
 * @author zifangsky
 * @date 2020/11/12
 * @since 1.0.0
 */
public enum BaseResultCode {
    /* 成功状态码 */
    SUCCESS("0000", "处理成功"),

    /* 参数错误：1001-1999 */
    PARAM_IS_INVALID("1001", "参数无效"),
    PARAM_IS_BLANK("1002", "参数为空"),
    PARAM_TYPE_BIND_ERROR("1003", "参数类型错误"),
    PARAM_NOT_COMPLETE("1004", "参数缺失"),
    PARAM_VALIDATION_FAILS("1005", "验签失败"),
    REFRESH_TOKEN_IS_INVALID("1006", "Refresh Token不可用"),

    /* 用户错误：2001-2999 */
    USER_NOT_LOGGED_IN("2001", "账号未登录"),
    USER_LOGIN_ERROR("2002", "账号名或密码错误"),
    USER_ACCOUNT_FORBIDDEN("2003", "账号已被禁用"),
    USER_ACCOUNT_IS_DEL("2004", "账号已注销"),
    USER_ACCOUNT_NOT_EXIST("2005", "账号不存在"),
    USER_ACCOUNT_HAS_EXISTED("2006", "账号已存在"),
    USER_ACCOUNT_NO_BALANCE("2007", "账户余额不足"),
    USER_ACCOUNT_IN_REVIEW("2008", "账号审核中"),
    USER_ACCOUNT_FALSE_REGISTER("2009", "账号审核未通过，请重新注册或联系管理员"),
    USER_PASSPORT_ERROR_TIMES_4("2010", "密码输入错误。如果输错次数超过4次，账户将被锁定"),
    USER_PASSPORT_ERROR_LOCK("2011", "密码输入错误次数过多，账户已被锁定"),

    //手机号
    USER_PHONE_ERROR("2101", "手机号错误"),
    USER_PHONE_FORBIDDEN("2102", "手机号已被禁用"),
    USER_PHONE_NOT_EXIST("2103", "手机号未注册"),
    USER_PHONE_HAS_EXISTED("2104", "手机号已注册"),
    USER_PHONE_PWD_SIMPLE("2105", "密码应由6~20位字母、数字或符号两种或三种组成"),
    //验证码
    USER_CODE_TIMEOUT("2201","验证码已失效，请重新获取"),
    USER_CODE_NULL("2202","还未获取验证码，请重新获取"),
    USER_CODE_ERROR_MORE("2203","验证码多次错误，请重新获取"),
    USER_CODE_COUNT_MORE("2204","验证码发送次数过多，请明天重试"),
    USER_CODE_SEND_ERROR("2205","验证码发送失败，请重新获取"),
    USER_CODE_VERIFY_ERROR("2206","验证码校验失败！"),

    //修改密码
    USER_MODIFY_PWD_ERROR("2301","原始密码错误"),
    USER_MODIFY_PWD2_ERROR("2302","新密码不一致"),
    USER_MODIFY_PWD3_ERROR("2303","修改密码失败"),

    /* 业务错误：3001-3999 */
    BUSINESS_INNER_ERROR("3001", "某业务出现问题"),

    /* 系统错误：4001-4999 */
    SYSTEM_INNER_ERROR("4001", "系统错误"),
    SYSTEM_IS_BUSY("4002", "系统繁忙，请稍后重试"),

    /* 数据错误：5001-59999 */
    DATA_NO_RESULTS("5001", "没有查询到结果"),
    DATA_IS_WRONG("5002", "数据有误"),
    DATA_ALREADY_EXISTED("5003", "数据已存在"),

    /* 接口错误：6001-6999 */
    INTERFACE_INNER_INVOKE_ERROR("6001", "内部系统接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR("6002", "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT("6003", "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID("6004", "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT("6005", "接口请求超时"),
    INTERFACE_EXCEED_LOAD("6006", "接口负载过高"),

    /* 权限错误：7001-7999 */
    PERMISSION_NO_ACCESS("7001", "无访问权限");

    private String code;

    private String msg;

    BaseResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    /**
     * 通过 code 获取对应枚举对象
     */
    public static BaseResultCode fromCode(String code) {
        for (BaseResultCode item : BaseResultCode.values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 通过 code 获取对应的状态码描述
     */
    public static String getMsgByCode(String code) {
        for (BaseResultCode item : BaseResultCode.values()) {
            if (item.code.equals(code)) {
                return item.msg;
            }
        }
        return null;
    }

//    /**
//     * 校验重复的code值
//     */
//    public static void main(String[] args) {
//        BaseResultCode[] resultCodes = BaseResultCode.values();
//        List<String> codeList = new ArrayList<>();
//
//        for (BaseResultCode resultCode : resultCodes) {
//            if (codeList.contains(resultCode.code)) {
//                System.out.println(resultCode.code);
//            } else {
//                codeList.add(resultCode.code());
//            }
//        }
//    }
}
