package cn.zifangsky.quickmodules.log.common;

/**
 * JDBCConstants
 *
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
public interface JDBCConstants extends Constants{
    /**
     * 日志表的分页查询条件
     */
    String LOG_FIND_ALL = "(content LIKE ? OR module LIKE ? OR username LIKE ?)";

}
