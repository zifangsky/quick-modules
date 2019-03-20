package cn.zifangsky.quickmodules.log.aop;

import cn.zifangsky.quickmodules.log.annotation.WebLog;
import cn.zifangsky.quickmodules.log.annotation.WebLogs;
import cn.zifangsky.quickmodules.log.model.SysLog;
import cn.zifangsky.quickmodules.log.model.UserInfo;
import cn.zifangsky.quickmodules.log.plugins.AbstractLogManager;
import cn.zifangsky.quickmodules.log.plugins.WebLogInfo;
import cn.zifangsky.quickmodules.log.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 用于记录请求日志（同时记录多个）
 *
 * @author zifangsky
 * @date 2017/12/5
 * @since 1.0.0
 */
@Aspect
@Component
public class MultipleLogPointcut {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Resource(name = "logServiceImpl")
    private LogService logService;

    @Autowired
    private WebLogInfo webLogInfo;

    @Around("@annotation(webLogs)")
    public Object doAround(ProceedingJoinPoint joinPoint, WebLogs webLogs) throws Throwable {
        Object result = null;
        Date start = new Date();

        //获取参数
        Object[] params = joinPoint.getArgs();
        //获取当前方法
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        //获取所有参数名
        String[] paramNames = discoverer.getParameterNames(method);
        //设置所有参数
        StandardEvaluationContext context = ParseExpressionUtils.setContextVariables(paramNames, params);

        //执行方法
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("请求发生异常，",throwable);
            throw throwable;
        }

        //结束时间
        Date end = new Date();

        UserInfo userInfo = null;
        if(webLogInfo != null){
            AbstractLogManager logManager = webLogInfo.getLogManager();
            if(logManager != null){
                //获取用户信息
                userInfo = logManager.getUserInfo();
            }
        }

        WebLog[] webLogArray = webLogs.value();

        if(webLogArray.length > 0){
            //遍历并分别记录日志
            for(WebLog webLog : webLogArray){
                //记录日志的条件
                Boolean condition = true;
                Boolean unless = false;

                if(StringUtils.isNoneBlank(webLog.condition())){
                    condition = ParseExpressionUtils.parseSpelValue(ParseExpressionUtils.removeSymbol(webLog.condition()), context, Boolean.class);
                }
                if(StringUtils.isNoneBlank(webLog.unless())){
                    unless = ParseExpressionUtils.parseSpelValue(ParseExpressionUtils.removeSymbol(webLog.unless()), context, Boolean.class);
                }

                //记录日志
                if(condition && !unless){
                    SysLog log = new SysLog();
                    log.setStartTime(start);

                    if(userInfo != null){
                        log.setUserId(userInfo.getUserId());
                        log.setUsername(userInfo.getUsername());
                    }

                    log.setContent(ParseExpressionUtils.parseValue(webLog.content(), context));
                    log.setModule(ParseExpressionUtils.parseValue(webLog.module(), context));
                    log.setType(webLog.type().getCode());
                    log.setTakeTime((end.getTime() - start.getTime()));

                    logService.addLog(log);
                }
            }
        }

        return result;
    }

}
