package cn.zifangsky.quickmodules.user.shiro;

import cn.zifangsky.quickmodules.common.common.SpringContextUtils;
import cn.zifangsky.quickmodules.user.enums.LoginTypes;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.model.bo.SysRoleBo;
import cn.zifangsky.quickmodules.user.plugins.PluginManager;
import cn.zifangsky.quickmodules.user.plugins.WebUserInfo;
import cn.zifangsky.quickmodules.user.service.UserService;
import cn.zifangsky.quickmodules.user.utils.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义{@link AuthorizingRealm}
 *
 * @author zifangsky
 * @date 2017/11/5
 * @since 1.0.0
 */
public class CustomRealm extends AuthorizingRealm {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Autowired
    private WebUserInfo webUserInfo;

    /**
     * 注入权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = null;
        SysUser sysUser = (SysUser) principalCollection.fromRealm(getName()).iterator().next();
        if(sysUser != null){
            info = new SimpleAuthorizationInfo();
            //通过用户ID查询角色权限信息
            Set<SysRoleBo> sysRoleBOS = userService.selectRoleBoByUserId(sysUser.getId());
            if(sysRoleBOS != null && sysRoleBOS.size() > 0){
                //所有角色名
                List<String> roleNames = new ArrayList<>();
                //所有权限的code集合
                Set<String> funcCodes = new HashSet<>();

                for(SysRoleBo sysRoleBO : sysRoleBOS){
                    //角色名
                    roleNames.add(sysRoleBO.getName());

                    Set<SysFunction> funcs = sysRoleBO.getFuncs();
                    if(funcs != null && funcs.size() > 0){
                        for(SysFunction f : funcs){
                            //权限CODE
                            if(StringUtils.isNoneBlank(f.getPathUrl())){
                                funcCodes.add(f.getPathUrl());
                            }

                        }
                    }
                }
                info.addRoles(roleNames);
                info.addStringPermissions(funcCodes);
            }
        }
        return info;
    }

    /**
     * 注入数据库中的认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        HttpServletRequest request = SpringContextUtils.getRequest();
        if(request != null){
            //登录模式
            String loginType = request.getParameter("type");

            if(StringUtils.isNoneBlank(loginType)){
                //获取用户名
                String username = (String) token.getPrincipal();
                //查询数据库中的用户记录
                SysUser sysUser = null;

                //使用“用户名+密码”登录
                if(LoginTypes.Username_Password.getCode().equals(Integer.valueOf(loginType))){
                    sysUser = userService.selectByUsername(username);
                }
                //使用“手机号码+验证码”登录
                else if(LoginTypes.Phone_Code.getCode().equals(Integer.valueOf(loginType))){
                    sysUser = userService.selectByPhone(username);
                }

                SimpleAuthenticationInfo info = null;
                if(sysUser != null){
                    info = new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), getName());

                    //是否删除该用户的旧session
                    if(webUserInfo.getDeleteOldSession()){
                        ShiroUtils.deleteOldSession(sysUser.getUsername());
                    }
                }

                return info;
            }
        }

        return null;
    }

    /**
     * 实际做密码校验的逻辑
     */
    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
            throws AuthenticationException {
        HttpServletRequest request = SpringContextUtils.getRequest();
        if(request != null){
            //登录模式
            String loginType = request.getParameter("type");

            if(StringUtils.isNoneBlank(loginType)){
                //获取用户名
                String username = (String) token.getPrincipal();
                //表单中的密码
                String tokenCredentials = String.valueOf((char[])token.getCredentials());
                PluginManager pluginManager = webUserInfo.getPluginManager();

                //使用“用户名+密码”登录
                if(LoginTypes.Username_Password.getCode().equals(Integer.valueOf(loginType))){
                    //数据库中的密码
                    String accountCredentials = (String)info.getCredentials();

                    //认证失败，抛出密码密码不正确的异常
                    if(!pluginManager.checkUserPassword(webUserInfo, tokenCredentials, accountCredentials)){
                        String msg = "Submitted credentials for token [" + token + "] did not match the expected credentials.";
                        throw new IncorrectCredentialsException(msg);
                    }
                }
                //使用“手机号码+验证码”登录
                else if(LoginTypes.Phone_Code.getCode().equals(Integer.valueOf(loginType))){
                    try {
                        //校验通过
                        if(!pluginManager.checkPhoneVerifyCode(username, tokenCredentials, request)){
                            String msg = "Submitted credentials for token [" + token + "] did not match the expected credentials.";
                            throw new IncorrectCredentialsException(msg);
                        }
                    }catch (Exception e){
                        logger.error(e.getMessage());
                        throw new UnsupportedTokenException(MessageFormat.format("手机号[{0}]在登录校验过程中出现异常！", username), e);
                    }
                }
            }
        }
    }

    /**
     * 清除缓存（当用户、角色、权限信息更新后，需要调用此方法清除缓存中的数据）
     */
    public void clearAuthorizationInfo(){
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
