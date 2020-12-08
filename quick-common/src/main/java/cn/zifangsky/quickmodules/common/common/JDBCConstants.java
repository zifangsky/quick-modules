package cn.zifangsky.quickmodules.common.common;

/**
 * JDBCConstants
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public interface JDBCConstants extends Constants{

    /**
     * 基本的删除SQL
     */
    String BASE_DELETE = "delete from {0} where {1} = ?";

    /**
     * 基本的假删除SQL
     */
    String BASE_DELETE_LOGICALLY = "update {0} set is_del = 1 where {1} = ?";

    /**
     * 基本的单条件查询SQL
     */
    String BASE_SELECT = "select * from {0} where {1} = ?";

    /**
     * 基本的通过多个条件查询SQL
     */
    String BASE_PARAMS_SELECT = "select * from {0} where 1=1";

    /**
     * 基本的统计SQL
     */
    String BASE_COUNT = "select count(*) from {0} where 1=1";

    /**
     * 基本的插入SQL
     */
    String BASE_INSERT = "insert into {0} ({1}) values ({2})";

    /**
     * 基本的更新SQL
     */
    String BASE_UPDATE = "update {0} set {1} where 1=1";

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
