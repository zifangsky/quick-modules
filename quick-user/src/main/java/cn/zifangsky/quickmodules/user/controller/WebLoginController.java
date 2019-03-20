package cn.zifangsky.quickmodules.user.controller;

import cn.zifangsky.quickmodules.common.common.SpringContextUtils;
import cn.zifangsky.quickmodules.user.annotations.PropertySourcedMapping;
import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.enums.LoginTypes;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录、注册相关
 *
 * @author zifangsky
 * @date 2017/11/12
 * @since 1.0.0
 */
@Controller
public class WebLoginController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;

    public WebLoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 生成图片验证码
     * @author zifangsky
     * @date 2017/11/12 14:13
     * @since 1.0.0
     */
    @RequestMapping("/generateAuthImage")
    @PropertySourcedMapping(propertyKey = "authImageUrl")
    public void generateAuthImage(HttpServletRequest request, HttpServletResponse response){
        try {
            this.userService.generateAuthImage(request, response);
        } catch (IOException e) {
            logger.error("生成图片验证码失败", e);
        }
    }

    /**
     * 发送手机验证码
     * @author zifangsky
     * @date 2017/11/12 16:10
     * @since 1.0.0
     */
    @RequestMapping("/sendLoginPhoneVerifyCode")
    @PropertySourcedMapping(propertyKey = "loginPhoneVerifyCodeUrl")
    public Map<String,Object> sendLoginPhoneVerifyCode(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(4);
        result.put("code",500);

        //手机号
        String phone = request.getParameter(Constants.FORM_PHONE);
        if(StringUtils.isBlank(phone)){
            result.put("msg","请求参数不能为空！");
            return  result;
        }else{
            try {
                logger.debug(MessageFormat.format("手机号[{0}]正在请求发送验证码", phone));
                this.userService.sendLoginPhoneVerifyCode(phone, request);
                result.put("code",200);
            } catch (Exception e) {
                logger.error("发送手机验证码失败", e);
                result.put("msg","发送手机验证码失败！");
            }
        }

        return  result;
    }

    /**
     * 登录验证
     * @author zifangsky
     * @date 2017/11/5 13:23
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PropertySourcedMapping(propertyKey = "loginCheckUrl")
    @ResponseBody
    public Map<String,Object> check(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(4);
        result.put("code",500);

        //登录类型
        String type = request.getParameter(Constants.FORM_LOGIN_TYPE);

        if(StringUtils.isBlank(type)){
            result.put("msg","请求参数不能为空！");
            return result;
        }else{
            try {
                SysUser user = null;

                //使用“用户名+密码”登录
                if(LoginTypes.Username_Password.getCode().equals(Integer.valueOf(type))){
                    //如果登录使用了验证码，则需要校验验证码
                    if(!userService.checkLoginVerifyCode(request)){
                        result.put("msg","验证码校验失败！");
                        return result;
                    }

                    //用户名
                    String username = request.getParameter(Constants.FORM_USERNAME);
                    //密码
                    String password = request.getParameter(Constants.FORM_PASSWORD);

                    if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
                        result.put("msg","请求参数不能为空！");
                        return result;
                    }else{
                        logger.debug(MessageFormat.format("用户[{0}]正在请求登录", username));
                        //查询数据库中的用户数据
                        user = userService.selectByUsername(username);

                        //如果账号状态不可用，则返回错误提示
                        if(!this.checkUserStatus(user, result)){
                            return result;
                        }

                        Subject subject = SecurityUtils.getSubject();
                        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

                        //1. 登录验证
                        subject.login(token);
                    }
                }
                //使用“手机号码+验证码”登录
                else if(LoginTypes.Phone_Code.getCode().equals(Integer.valueOf(type))){
                    //手机号
                    String phone = request.getParameter(Constants.FORM_PHONE);
                    //验证码
                    String code = request.getParameter(Constants.FORM_PHONE_CODE);

                    if(StringUtils.isBlank(phone) || StringUtils.isBlank(code)){
                        result.put("msg","请求参数不能为空！");
                        return result;
                    }else{
                        logger.debug(MessageFormat.format("用户[{0}]正在请求登录", phone));
                        //查询数据库中的用户数据
                        user = userService.selectByPhone(phone);

                        //如果账号状态不可用，则返回错误提示
                        if(!this.checkUserStatus(user, result)){
                            return result;
                        }

                        Subject subject = SecurityUtils.getSubject();
                        UsernamePasswordToken token = new UsernamePasswordToken(phone, code);

                        //1. 登录验证
                        subject.login(token);
                    }
                }

                //2. session中添加用户信息
                HttpSession session = request.getSession();
                session.setAttribute(Constants.SESSION_USER, user);

                //3. 更新用户表，记录登录时间和登录IP
                user.setLoginTime(new Date());
                user.setLoginIp(SpringContextUtils.getRequestIp(request));
                userService.updateUser(user);

                //4. 返回给页面的数据
                result.put("code",200);
                //登录成功之后的回调地址
                String redirectUrl = (String) session.getAttribute(Constants.SESSION_LOGIN_REDIRECT_URL);
                session.removeAttribute(Constants.SESSION_LOGIN_REDIRECT_URL);

                if(StringUtils.isNoneBlank(redirectUrl)){
                    result.put("redirect_uri", redirectUrl);
                }
            }catch (Exception e){
                result.put("code", 500);
                result.put("msg", "登录失败，用户名或密码错误！");

                logger.error("登录失败",e);
            }
        }

        return result;
    }

    /**
     * 退出登录
     * @author zifangsky
     * @date 2017/11/12 17:44
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PropertySourcedMapping(propertyKey = "logoutUrl")
    @ResponseBody
    public Map<String,Object> logout(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(1);
        HttpSession session = request.getSession();
        SysUser user = (SysUser) session.getAttribute(Constants.SESSION_USER);

        if(user != null){
            logger.debug(MessageFormat.format("用户[{0}]正在退出登录", user.getUsername()));
        }

        try {
            //1. 移除session中的数据
            session.removeAttribute(Constants.SESSION_USER);

            //2. 退出登录
            Subject subject = SecurityUtils.getSubject();
            subject.logout();

            //3. 返回状态码
            result.put("code", 200);
        }catch (Exception e){
            result.put("code",500);
        }

        return result;
    }

    /**
     * 检查数据库中的用户状态
     * @author zifangsky
     * @date 2017/11/6 17:44
     * @since 1.0.0
     * @param user 用户
     * @param result 返回Map
     * @return boolean
     */
    private boolean checkUserStatus(SysUser user, Map<String,Object> result) {
        if(user == null){
            result.put("msg","用户不存在！");
            return false;
        }else{
            if(user.getIsDel()){
                result.put("msg", "该用户已注销!");
                return false;
            }

            switch (user.getStatus()){
                case 1:
                    result.put("msg", "该用户已锁定!");
                    return false;
                case 2:
                    result.put("msg","该用户还未开通!");
                    return false;
                default:
                    return true;
            }
        }
    }

}
