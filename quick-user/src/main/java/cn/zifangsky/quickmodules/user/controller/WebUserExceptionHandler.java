package cn.zifangsky.quickmodules.user.controller;

import cn.zifangsky.quickmodules.common.common.SpringContextUtils;
import cn.zifangsky.quickmodules.common.utils.JsonUtils;
import cn.zifangsky.quickmodules.user.enums.AuthCodeEnums;
import cn.zifangsky.quickmodules.user.plugins.WebUserInfo;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author zifangsky
 * @date 2017/1/3
 * @since 1.0.0
 */
@ControllerAdvice
public class WebUserExceptionHandler {

    @Autowired
    private WebUserInfo webUserInfo;

    /**
     * 登录异常
     * @author zifangsky
     * @date 2017/1/3 15:44
     * @since 1.0.0
     * @return java.lang.String
     */
    @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
    public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
        if (SpringContextUtils.isAjaxRequest(request)) {
            try {
                //没有登录，则返回Ajax提示信息
                this.generateErrorResponse(response, AuthCodeEnums.UN_LOGIN);
            } catch (Exception e) {
                //ignore
            }

            return null;
        } else {
            return "redirect:" + webUserInfo.getLoginUrl();
        }
    }

    /**
     * 权限方面的异常
     * @author zifangsky
     * @date 2017/1/3 15:55
     * @since 1.0.0
     * @return java.lang.String
     */
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
        if (SpringContextUtils.isAjaxRequest(request)) {
            try {
                //没有权限，则返回Ajax提示信息
                this.generateErrorResponse(response, AuthCodeEnums.NO_PERMISSIONS);
            } catch (Exception e) {
                //ignore
            }

            return null;
        } else {
            return "redirect:" + webUserInfo.getUnauthorizedUrl();
        }
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
