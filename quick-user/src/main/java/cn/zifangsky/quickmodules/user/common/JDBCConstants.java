package cn.zifangsky.quickmodules.user.common;

/**
 * JDBCConstants
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public interface JDBCConstants extends Constants{
    /**
     * 用户表的分页查询条件
     */
    String USER_FIND_ALL = "is_del = 0 and (username LIKE ? or name LIKE ? or phone LIKE ? or email LIKE ?)";

    /**
     * 角色表的分页查询条件
     */
    String ROLE_FIND_ALL = "status = 0 and name LIKE ?";

    /**
     * 资源表的分页查询条件
     */
    String FUNC_FIND_ALL = "state = 0 and name LIKE ?";
}
