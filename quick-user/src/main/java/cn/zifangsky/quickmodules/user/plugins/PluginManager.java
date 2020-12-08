package cn.zifangsky.quickmodules.user.plugins;

import cn.zifangsky.quickmodules.common.utils.StringUtils;
import cn.zifangsky.quickmodules.common.utils.VerifyCodeUtils;
import cn.zifangsky.quickmodules.user.common.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用于暴露出需要自定义的方法，如：手机验证码的生成/校验、密码校验逻辑、密码登录验证码的生成/校验
 *
 * @author zifangsky
 * @date 2017/11/12
 * @since 1.0.0
 */
public class PluginManager {

    /**
     * “用户名+密码”模式，校验自定义的密码加密方式
     * @author zifangsky
     * @date 2020/1/3 16:15
     * @since 1.0.0
     * @param correctPassword   数据库中正确的密码
     * @param validatedPassword 来至外部待验证的密码
     * @return boolean
     */
    public boolean checkCustomUsernamePasswordValidatedInfo(String correctPassword, String validatedPassword, HttpServletRequest request) {
        return false;
    }

    /**
     * 发送登录时的短信验证码（使用的时候自行实现）
     * @author zifangsky
     * @date 2017/11/12 13:38
     * @since 1.0.0
     * @param phone 手机号码
     */
    public void sendLoginPhoneVerifyCode(String phone, HttpServletRequest request) throws Exception {
        throw new RuntimeException("请复写此方法后使用");
    }

    /**
     * 发送注册时的短信验证码（使用的时候自行实现）
     * @author zifangsky
     * @date 2017/11/12 13:38
     * @since 1.0.0
     * @param phone 手机号码
     */
    public void sendRegisterPhoneVerifyCode(String phone, HttpServletRequest request) throws Exception {
        throw new RuntimeException("请复写此方法后使用");
    }

    /**
     * 校验指定手机号的验证码
     * @author zifangsky
     * @date 2017/11/12 13:43
     * @since 1.0.0
     * @param phone 手机号码
     * @param formCode 来至Form表单中的手机验证码
     * @return boolean
     */
    public boolean checkPhoneVerifyCode(String phone, String formCode, HttpServletRequest request) throws Exception{
//        System.out.println(phone + "**********" + formCode);
//        return true;
        throw new RuntimeException("请复写此方法后使用");
    }

    /**
     * 生成图片验证码
     * @author zifangsky
     * @date 2017/11/12 14:13
     * @since 1.0.0
     */
    public void generateAuthImage(HttpServletRequest request, HttpServletResponse response, Integer width, Integer height) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        //生成随机字符串
        String verifyCode = StringUtils.generateVerifyCode(5);
        HttpSession session = request.getSession();
        session.setAttribute(Constants.SESSION_VERIFY_CODE, verifyCode);
        VerifyCodeUtils.outputImage(width, height, response.getOutputStream(), verifyCode);
    }

    /**
     * 校验图片验证码
     * @author zifangsky
     * @date 2017/11/12 14:41
     * @since 1.0.0
     * @param formCode 来至Form表单中的验证码
     * @return boolean
     */
    public boolean checkVerifyCode(HttpServletRequest request, String formCode){
        HttpSession session = request.getSession();

        //session中的验证码
        String codeFromSession = (String) session.getAttribute(Constants.SESSION_VERIFY_CODE);
        //使用之后删除
        session.removeAttribute(Constants.SESSION_VERIFY_CODE);

        return org.apache.commons.lang3.StringUtils.isNoneBlank(formCode) && formCode.toUpperCase().equals(codeFromSession);
    }



}
