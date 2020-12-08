package cn.zifangsky.quickmodules.user.controller;

import cn.zifangsky.easylimit.access.Access;
import cn.zifangsky.easylimit.access.impl.ExposedTokenAccess;
import cn.zifangsky.easylimit.authc.ValidatedInfo;
import cn.zifangsky.easylimit.authc.impl.PhoneCodeValidatedInfo;
import cn.zifangsky.easylimit.authc.impl.UsernamePasswordValidatedInfo;
import cn.zifangsky.easylimit.session.impl.support.SimpleAccessToken;
import cn.zifangsky.easylimit.session.impl.support.SimpleRefreshToken;
import cn.zifangsky.easylimit.utils.SecurityUtils;
import cn.zifangsky.quickmodules.common.common.Holder;
import cn.zifangsky.quickmodules.common.common.resp.BaseResultCode;
import cn.zifangsky.quickmodules.common.common.resp.Result;
import cn.zifangsky.quickmodules.common.common.resp.ResultUtils;
import cn.zifangsky.quickmodules.common.utils.BeanUtils;
import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.common.SpringContextUtils;
import cn.zifangsky.quickmodules.user.enums.LoginTypes;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.model.vo.UserInfo;
import cn.zifangsky.quickmodules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2020/11/17
 * @since 1.1.0
 */
@Slf4j
@Controller
public class WebLoginController {
    private UserService userService;

    public WebLoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 生成图片验证码
     * @author zifangsky
     * @date 2018/11/12 14:13
     * @since 1.0.0
     */
    @RequestMapping("/generateAuthImage")
    public void generateAuthImage(HttpServletRequest request, HttpServletResponse response){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/generateAuthImage」接口，tid=[%s]", tid));
        try {
            this.userService.generateAuthImage(request, response);
        } catch (IOException e) {
            log.error("生成图片验证码失败", e);
        }
    }

    /**
     * 发送手机验证码
     * @author zifangsky
     * @date 2018/11/12 16:10
     * @since 1.0.0
     */
    @RequestMapping("/sendLoginPhoneVerifyCode")
    public Result<Object> sendLoginPhoneVerifyCode(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/sendLoginPhoneVerifyCode」接口，tid=[%s]", tid));
        Map<String,Object> dataMap;

        //手机号
        String phone = request.getParameter(Constants.FORM_PHONE);
        if(StringUtils.isBlank(phone)){
            dataMap = new HashMap<>(4);
            dataMap.put("msg","请求参数「phone」不能为空！");
            return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
        }else{
            try {
                log.debug(MessageFormat.format("手机号[{0}]正在请求发送验证码", phone));
                this.userService.sendLoginPhoneVerifyCode(phone, request);
            } catch (Exception e) {
                log.error("发送手机验证码失败", e);
                return ResultUtils.error(BaseResultCode.USER_CODE_SEND_ERROR, tid);
            }
        }

        return ResultUtils.success(tid);
    }

    /**
     * 登录验证
     * @author zifangsky
     * @date 2018/11/5 13:23
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> check(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/check」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        //登录类型
        String type = request.getParameter(Constants.FORM_LOGIN_TYPE);

        if(StringUtils.isBlank(type)){
            dataMap.put("msg","请求参数「type」不能为空！");
            return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
        }else{
            try {
                SysUser user = null;
                //获取本次请求实例
                ExposedTokenAccess access = null;

                //使用“用户名+密码”登录
                if(LoginTypes.Username_Password.getCode().equals(Integer.valueOf(type))){
                    //如果登录使用了验证码，则需要校验验证码
                    if(!userService.checkLoginVerifyCode(request)){
                        return ResultUtils.error(BaseResultCode.USER_CODE_VERIFY_ERROR, tid);
                    }

                    //用户名
                    String username = request.getParameter(Constants.FORM_USERNAME);
                    //密码
                    String password = request.getParameter(Constants.FORM_PASSWORD);

                    if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
                        dataMap.put("msg","请求参数「username」或者「password」不能为空！");
                        return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
                    }else{
                        log.debug(MessageFormat.format("用户[{0}]正在请求登录", username));
                        //查询数据库中的用户数据
                        user = userService.selectByUsername(username);

                        //如果账号状态不可用，则返回错误提示
                        Holder<BaseResultCode> resultCodeHolder = new Holder<>();
                        if(!this.checkUserStatus(user, resultCodeHolder)){
                            return ResultUtils.error(resultCodeHolder.value, tid);
                        }

                        access = (ExposedTokenAccess) SecurityUtils.getAccess();
                        //设置验证信息
                        ValidatedInfo validatedInfo = new UsernamePasswordValidatedInfo(username, password, userService.getEncryptionType());

                        //1. 登录验证
                        access.login(validatedInfo);
                    }
                }
                //使用“手机号码+验证码”登录
                else if(LoginTypes.Phone_Code.getCode().equals(Integer.valueOf(type))){
                    //手机号
                    String phone = request.getParameter(Constants.FORM_PHONE);
                    //验证码
                    String code = request.getParameter(Constants.FORM_PHONE_CODE);

                    if(StringUtils.isBlank(phone) || StringUtils.isBlank(code)){
                        dataMap.put("msg","请求参数「phone」或者「code」不能为空！");
                        return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
                    }else{
                        log.debug(MessageFormat.format("用户[{0}]正在请求登录", phone));
                        //查询数据库中的用户数据
                        user = userService.selectByPhone(phone);

                        //如果账号状态不可用，则返回错误提示
                        Holder<BaseResultCode> resultCodeHolder = new Holder<>();
                        if(!this.checkUserStatus(user, resultCodeHolder)){
                            return ResultUtils.error(resultCodeHolder.value, tid);
                        }

                        access = (ExposedTokenAccess) SecurityUtils.getAccess();
                        //设置验证信息
                        ValidatedInfo validatedInfo = new PhoneCodeValidatedInfo(phone, code);

                        //1. 登录验证
                        access.login(validatedInfo);
                    }
                }

                //2. session中添加用户信息
                HttpSession session = request.getSession();
                session.setAttribute(Constants.SESSION_USER, user);

                //3. 更新用户表，记录登录时间和登录IP
                user.setLoginTime(new Date());
                user.setLoginIp(SpringContextUtils.getRequestIp(request));
                userService.updateUser(user);

                //4. 获取Access Token和Refresh Token
                SimpleAccessToken accessToken = access.getAccessToken();
                SimpleRefreshToken refreshToken = access.getRefreshToken();

                //5. 返回给页面的数据
                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(user, userInfo);
                dataMap.put("access_token", accessToken.getAccessToken());
                dataMap.put("refresh_token", refreshToken.getRefreshToken());
                dataMap.put("expires_in", accessToken.getExpiresIn());
                dataMap.put("user_info", userInfo);
            }catch (Exception e){
                log.error("登录失败！", e);
                return ResultUtils.error(BaseResultCode.USER_LOGIN_ERROR, tid);
            }
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 刷新Access Token
     *
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author zifangsky
     * @date 2019/5/29 13:23
     * @since 1.0.0
     */
    @RequestMapping(value = "/refreshToken", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> refreshAccessToken(HttpServletRequest request) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/refreshToken」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        try {
            //Refresh Token
            String refreshTokenStr = request.getParameter("refresh_token");
            if(StringUtils.isBlank(refreshTokenStr)){
                dataMap.put("msg","请求参数「refresh_token」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }

            //获取本次请求实例
            ExposedTokenAccess access = (ExposedTokenAccess) SecurityUtils.getAccess();

            //1. 刷新Access Token
            SimpleAccessToken newAccessToken = access.refreshAccessToken(refreshTokenStr);

            //2. 返回给页面的数据
            dataMap.put("access_token", newAccessToken.getAccessToken());
            dataMap.put("expires_in", newAccessToken.getExpiresIn());
            dataMap.put("refresh_token", refreshTokenStr);
        } catch (Exception e) {
            log.error("Refresh Token不可用", e);
            return ResultUtils.error(BaseResultCode.REFRESH_TOKEN_IS_INVALID, tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 退出登录
     * @author zifangsky
     * @date 2018/11/12 17:44
     * @since 1.0.0
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> logout(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/logout」接口，tid=[%s]", tid));

        HttpSession session = request.getSession();
        SysUser user = (SysUser) session.getAttribute(Constants.SESSION_USER);

        if(user != null){
            log.debug(MessageFormat.format("用户[{0}]正在退出登录", user.getUsername()));
        }

        try {
            //1. 移除session中的数据
            session.removeAttribute(Constants.SESSION_USER);

            //2. 退出登录
            Access access = SecurityUtils.getAccess();
            access.logout();

        }catch (Exception e){
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid);
    }

    /**
     * 检查数据库中的用户状态
     * @author zifangsky
     * @date 2018/11/6 17:44
     * @since 1.0.0
     * @param user 用户
     * @param resultCodeHolder 返回异常类型
     * @return boolean
     */
    private boolean checkUserStatus(SysUser user, Holder<BaseResultCode> resultCodeHolder) {
        if(user == null){
            resultCodeHolder.value = BaseResultCode.USER_ACCOUNT_NOT_EXIST;
            return false;
        }else{
            if(user.getIsDel()){
                resultCodeHolder.value = BaseResultCode.USER_ACCOUNT_IS_DEL;
                return false;
            }

            switch (user.getStatus()){
                case 1:
                    resultCodeHolder.value = BaseResultCode.USER_ACCOUNT_FORBIDDEN;
                    return false;
                case 2:
                    resultCodeHolder.value = BaseResultCode.USER_ACCOUNT_IN_REVIEW;
                    return false;
                default:
                    return true;
            }
        }
    }

}
