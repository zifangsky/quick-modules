package cn.zifangsky.quickmodules.log.service.impl;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.log.mapper.LogMapper;
import cn.zifangsky.quickmodules.log.model.SysLog;
import cn.zifangsky.quickmodules.log.plugins.WebLogInfo;
import cn.zifangsky.quickmodules.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.xml.ws.Holder;
import java.util.List;

/**
 * 日志相关服务
 *
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
@Service("logServiceImpl")
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private WebLogInfo logInfo;

    @Async("logTaskExecutor")
    @Override
    public void addLog(SysLog log) {
        if(logInfo.isEnableCache()){
            //添加到缓存，定时消费
            logInfo.getCacheManager().addCache(log);
        }else{
            logMapper.insertSelective(log);
        }
    }

    @Override
    public List<SysLog> findAll(Long roleId, String queryParam, Holder<PageInfo> pageInfoHolder) {
        return logMapper.findAll(roleId, queryParam, pageInfoHolder);
    }
}
