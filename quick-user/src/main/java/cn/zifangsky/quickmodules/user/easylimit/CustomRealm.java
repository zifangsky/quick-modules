package cn.zifangsky.quickmodules.user.easylimit;

import cn.zifangsky.easylimit.authc.PrincipalInfo;
import cn.zifangsky.easylimit.authc.ValidatedInfo;
import cn.zifangsky.easylimit.authc.impl.PhoneCodeValidatedInfo;
import cn.zifangsky.easylimit.authc.impl.SimplePrincipalInfo;
import cn.zifangsky.easylimit.authc.impl.UsernamePasswordValidatedInfo;
import cn.zifangsky.easylimit.exception.authc.AuthenticationException;
import cn.zifangsky.easylimit.permission.PermissionInfo;
import cn.zifangsky.easylimit.permission.impl.SimplePermissionInfo;
import cn.zifangsky.easylimit.realm.impl.AbstractPermissionRealm;
import cn.zifangsky.quickmodules.user.common.SpringContextUtils;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.model.bo.SysRoleBo;
import cn.zifangsky.quickmodules.user.plugins.PluginManager;
import cn.zifangsky.quickmodules.user.plugins.WebUserInfo;
import cn.zifangsky.quickmodules.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义{@link cn.zifangsky.easylimit.realm.Realm}
 *
 * @author zifangsky
 * @date 2020/1/3
 * @since 1.0.0
 */
public class CustomRealm extends AbstractPermissionRealm {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;

    private WebUserInfo webUserInfo;

    public CustomRealm(UserService userService, WebUserInfo webUserInfo) {
        this.userService = userService;
        this.webUserInfo = webUserInfo;
    }

    /**
     * 自定义“角色+权限”信息的获取方式
     */
    @Override
    protected PermissionInfo doGetPermissionInfo(PrincipalInfo principalInfo) {
        SimplePermissionInfo permissionInfo = null;

        //获取用户信息
        SysUser sysUser = (SysUser) principalInfo.getPrincipal();
        if (sysUser != null) {
            //通过用户ID查询角色权限信息
            Set<SysRoleBo> sysRoleBOS = userService.selectRoleBoByUserId(sysUser.getId());

            if (sysRoleBOS != null && sysRoleBOS.size() > 0) {
                //所有角色名
                Set<String> roleNames = new HashSet<>();
                //所有权限的code集合
                Set<String> funcCodes = new HashSet<>();

                for (SysRoleBo sysRoleBO : sysRoleBOS) {
                    //角色名
                    roleNames.add(sysRoleBO.getName());

                    Set<SysFunction> funcs = sysRoleBO.getFuncs();
                    if (funcs != null && funcs.size() > 0) {
                        for (SysFunction f : funcs) {
                            //权限CODE
                            if (StringUtils.isNoneBlank(f.getPathUrl())) {
                                funcCodes.add(f.getPathUrl());
                            }
                        }
                    }
                }
                //实例化
                permissionInfo = new SimplePermissionInfo(roleNames, funcCodes);
            }
        }

        return permissionInfo;
    }

    /**
     * 自定义从表单的验证信息获取数据库中正确的用户主体信息
     */
    @Override
    protected PrincipalInfo doGetPrincipalInfo(ValidatedInfo validatedInfo) throws AuthenticationException {
        //查询数据库中的用户记录
        SysUser sysUser = null;

        //使用“用户名+密码”登录
        if (validatedInfo instanceof UsernamePasswordValidatedInfo) {
            UsernamePasswordValidatedInfo usernamePasswordValidatedInfo = (UsernamePasswordValidatedInfo) validatedInfo;
            sysUser = userService.selectByUsername(usernamePasswordValidatedInfo.getUsername());
        }
        //使用“手机号码+验证码”登录
        else if (validatedInfo instanceof PhoneCodeValidatedInfo) {
            PhoneCodeValidatedInfo phoneCodeValidatedInfo = (PhoneCodeValidatedInfo) validatedInfo;
            sysUser = userService.selectByPhone(phoneCodeValidatedInfo.getPhone());
        }

        SimplePrincipalInfo principalInfo = null;
        if (sysUser != null) {
            principalInfo = new SimplePrincipalInfo(sysUser, sysUser.getUsername(), sysUser.getPassword());
        }

        return principalInfo;
    }

    /**
     * 校验手机短信验证码
     */
    @Override
    protected boolean verifyPhoneCode(PhoneCodeValidatedInfo phoneCodeValidatedInfo, PrincipalInfo principalInfo) {
        PluginManager pluginManager = webUserInfo.getPluginManager();
        HttpServletRequest request = SpringContextUtils.getRequest();

        boolean result = false;
        try {
            result = pluginManager.checkPhoneVerifyCode(phoneCodeValidatedInfo.getPhone(), phoneCodeValidatedInfo.getCode(), request);
        } catch (Exception e) {
            logger.error("校验手机短信验证码失败：", e);
        }

        return result;
    }

    /**
     * “用户名+密码”模式，校验自定义的密码加密方式
     */
    @Override
    protected boolean verifyCustomUsernamePasswordValidatedInfo(UsernamePasswordValidatedInfo usernamePasswordValidatedInfo, PrincipalInfo principalInfo) {
        PluginManager pluginManager = webUserInfo.getPluginManager();
        HttpServletRequest request = SpringContextUtils.getRequest();

        return pluginManager.checkCustomUsernamePasswordValidatedInfo(principalInfo.getPassword(), usernamePasswordValidatedInfo.getPassword(), request);
    }
}
