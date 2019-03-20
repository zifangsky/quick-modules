package cn.zifangsky.quickmodules.log.mapper;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.log.model.SysLog;

import javax.xml.ws.Holder;
import java.util.List;

public interface LogMapper {
    int deleteLogically(Integer id);

    int delete(Long id);

    int insert(SysLog log);

    int insertSelective(SysLog log);

    /**
     * 向数据库中批量插入日志
     * @author zifangsky
     * @date 2017/12/13 16:16
     * @since 1.0.0
     * @param list 日志集合
     */
    void insertBatchSelective(List<SysLog> list);

    SysLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLog log);

    int updateByPrimaryKey(SysLog log);

    /**
     * 分页查询
     * @author zifangsky
     * @date 2017/12/5 10:10
     * @since 1.0.0
     * @param roleId 角色ID
     * @param queryParam 查询参数
     * @param pageInfoHolder 分页参数的包装类
     * @return java.util.List<cn.zifangsky.quickmodules.log.model.SysLog>
     */
    List<SysLog> findAll(Long roleId, String queryParam, Holder<PageInfo> pageInfoHolder);
}