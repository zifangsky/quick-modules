package cn.zifangsky.quickmodules.user.shiro;

import cn.zifangsky.quickmodules.common.utils.JsonUtils;
import cn.zifangsky.quickmodules.user.enums.AuthCodeEnums;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 前后端分离项目的登录过滤器
 *
 * @author zifangsky
 * @date 2017/1/3
 * @since 1.0.0
 */
public class AjaxFormAuthenticationFilter extends FormAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(AjaxFormAuthenticationFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }

            //没有登录，则返回Ajax提示信息
            this.generateErrorResponse((HttpServletResponse)response, AuthCodeEnums.UN_LOGIN);

            return false;
        }
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        this.generateErrorResponse((HttpServletResponse)response, AuthCodeEnums.LOGIN_SUCCESS);

        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        try {
            this.generateErrorResponse((HttpServletResponse)response, AuthCodeEnums.LOGIN_FAILURE);
        } catch (Exception e1) {
            //ignore
        }

        return false;
    }

    /**
     * 组装错误请求的返回
     */
    private void generateErrorResponse(HttpServletResponse response, AuthCodeEnums errorEnum) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        Map<String,Object> result = new HashMap<>(2);
        result.put("code", errorEnum.getCode());
        result.put("msg", errorEnum.getMsg());

        response.getWriter().write(JsonUtils.toJson(result));
    }
}
