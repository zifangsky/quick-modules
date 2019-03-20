package cn.zifangsky.quickmodules.log.enums;

/**
 * 日志类型
 *
 * @author zifangsky
 * @date 2017/12/4
 * @since 1.0.0
 */
public enum LogTypes {
    /**
     * 操作日志
     */
    OPERATION(0, "操作日志"),
    /**
     * 业务日志
     */
    BUSINESS(1, "业务日志");

    private Integer code;

    private String description;

    LogTypes(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
