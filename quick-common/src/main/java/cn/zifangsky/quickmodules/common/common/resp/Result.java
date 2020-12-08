package cn.zifangsky.quickmodules.common.common.resp;

import cn.zifangsky.quickmodules.common.utils.DateUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回报文格式
 *
 * @author zifangsky
 * @date 2020/11/12
 * @since 1.0.0
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -7656717461702492726L;
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回描述
     */
    private String msg;
    /**
     * 返回tid
     */
    private String tid;
    /**
     * 时间
     */
    private String time;
    /**
     * 返回正文
     */
    private T data;

    public Result(BaseResultCode baseResultCode, String tid) {
        this(baseResultCode.code(), baseResultCode.msg(), tid);
    }

    public Result(String code, String msg, String tid) {
        this(code, msg, tid, null);
    }

    public Result(BaseResultCode baseResultCode, String tid, T data) {
        this(baseResultCode.code(), baseResultCode.msg(), tid, data);
    }

    public Result(String code, String msg, String tid, T data) {
        this.code = code;
        this.msg = msg;
        this.tid = tid;
        this.data = data;
        this.time = DateUtils.nowStr();
    }
}