package cn.zifangsky.quickmodules.log.service;

import cn.zifangsky.quickmodules.common.common.Holder;
import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.log.model.SysLog;

import java.util.List;

/**
 * 日志相关服务
 *
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
public interface LogService {
    /**
     * 新增日志
     * @author zifangsky
     * @date 2017/12/5 10:09
     * @since 1.0.0
     * @param log 日志详情
     */
    void addLog(SysLog log);

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
