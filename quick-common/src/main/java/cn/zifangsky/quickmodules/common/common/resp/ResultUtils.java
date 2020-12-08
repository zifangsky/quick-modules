package cn.zifangsky.quickmodules.common.common.resp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 定义统一的API接口返回格式
 *
 * @author zifangsky
 * @date 2020/11/12
 * @since 1.0.0
 */
@Component
public class ResultUtils {

    @Autowired
    private TidGenerator autoTidGenerator;

    private static TidGenerator tidGenerator;

    @PostConstruct
    public void init(){
        tidGenerator = autoTidGenerator;
    }

    /**
     * 返回一个可用的TID
     */
    public static String nextTid(){
        return tidGenerator.nextTid();
    }

    /**
     * 返回成功标识
     * @param tid TID
     */
    public static Result<Object> success(String tid) {
        return new Result<>(BaseResultCode.SUCCESS, tid);
    }

    /**
     * 返回成功标识
     * @param tid TID
     * @param data 数据域
     */
    public static <T> Result<T> success(String tid, T data) {
        return new Result<>(BaseResultCode.SUCCESS, tid, data);
    }

    /**
     * 返回默认的系统错误
     * @param tid TID
     */
    public static Result<Object> error(String tid) {
        return new Result<>(BaseResultCode.SYSTEM_INNER_ERROR, tid);
    }

    /**
     * 返回默认的系统错误
     * @param tid TID
     * @param data 数据域
     */
    public static <T> Result<T> error(String tid, T data) {
        return new Result<>(BaseResultCode.SYSTEM_INNER_ERROR, tid, data);
    }

    /**
     * 返回指定错误
     * @param errorCode 指定错误码
     * @param tid TID
     */
    public static <T> Result<T> error(BaseResultCode errorCode, String tid) {
        return new Result<>(errorCode, tid);
    }

    /**
     * 返回指定错误
     * @param errorCode 指定错误码
     * @param tid TID
     * @param data 数据域
     */
    public static <T> Result<T> error(BaseResultCode errorCode, String tid, T data) {
        return new Result<>(errorCode, tid, data);
    }

    /**
     * 返回指定错误
     * @param code 指定错误码
     * @param msg 指定错误描述
     * @param tid TID
     */
    public static Result<Object> error(String code, String msg, String tid) {
        return new Result<>(code, msg, tid);
    }

    /**
     * 返回指定错误
     * @param code 指定错误码
     * @param msg 指定错误描述
     * @param tid TID
     * @param data 数据域
     */
    public static <T> Result<T> error(String code, String msg, String tid, T data) {
        return new Result<>(code, msg, tid, data);
    }

}
