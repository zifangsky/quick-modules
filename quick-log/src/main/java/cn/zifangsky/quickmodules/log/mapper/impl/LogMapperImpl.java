package cn.zifangsky.quickmodules.log.mapper.impl;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.common.plugins.DbUtilsTemplate;
import cn.zifangsky.quickmodules.log.common.Constants;
import cn.zifangsky.quickmodules.log.common.JDBCConstants;
import cn.zifangsky.quickmodules.log.mapper.LogMapper;
import cn.zifangsky.quickmodules.log.model.SysLog;
import cn.zifangsky.quickmodules.log.plugins.WebLogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志相关表的基本操作
 *
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
@Repository
public class LogMapperImpl implements LogMapper {

    @Autowired
    private WebLogInfo webLogInfo;

    @Resource(name = "logDbUtilsTemplate")
    private DbUtilsTemplate template;

    @Override
    public int deleteLogically(Integer id) {
        return template.deleteLogically(Constants.TABLE_LOG, "id", id);
    }

    @Override
    public int delete(Long id) {
        return template.delete(Constants.TABLE_LOG, "id", id);
    }

    @Override
    public int insert(SysLog log) {
        return template.insert(Constants.TABLE_LOG, log);
    }

    @Override
    public int insertSelective(SysLog log) {
        return template.insertSelective(Constants.TABLE_LOG, log);
    }

    @Override
    public void insertBatchSelective(List<SysLog> list) {
        template.insertBatchSelective(Constants.TABLE_LOG, list);
    }

    @Override
    public SysLog selectByPrimaryKey(Integer id) {
        return template.select(Constants.TABLE_LOG, "id", id, SysLog.class);
    }

    @Override
    public int updateByPrimaryKeySelective(SysLog log) {
        return template.updateSelective(Constants.TABLE_LOG, log, "id", log.getId());
    }

    @Override
    public int updateByPrimaryKey(SysLog log) {
        return template.update(Constants.TABLE_LOG, log, "id", log.getId());
    }

    @Override
    public List<SysLog> findAll(Long roleId, String queryParam, Holder<PageInfo> pageInfoHolder) {
        List<Object> valuesList = new ArrayList<>();
        if(queryParam != null){
            queryParam = queryParam.replace("%", "");
        }else{
            queryParam = "";
        }

        String querySql;

        //查询指定角色的日志
        if(roleId != null && roleId > 0){
            valuesList.add(roleId);
            querySql = webLogInfo.getSelectByRolePrefixSql() + " AND " + JDBCConstants.LOG_FIND_ALL;
        }else{
            querySql = JDBCConstants.LOG_FIND_ALL;
        }

        valuesList.add("%" + queryParam + "%");
        valuesList.add("%" + queryParam + "%");
        valuesList.add("%" + queryParam + "%");

        return template.findAll(Constants.TABLE_LOG, querySql, valuesList, pageInfoHolder, SysLog.class);
    }
}
