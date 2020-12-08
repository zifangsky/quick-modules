package cn.zifangsky.quickmodules.user.service;

import cn.zifangsky.easylimit.enums.EncryptionTypeEnums;
import cn.zifangsky.quickmodules.common.common.Holder;
import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysRole;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.model.bo.SysRoleBo;
import cn.zifangsky.quickmodules.user.model.bo.SysUserRoleBo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 用户相关Service
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public interface UserService {
    /**
     * 获取“用户名+密码”模式下的密码加密方式
     *
     * @return cn.zifangsky.easylimit.enums.EncryptionTypeEnums
     * @author zifangsky
     * @date 2020/1/3 17:21
     * @since 1.0.0
     */
    EncryptionTypeEnums getEncryptionType();

    /**
     * 通过用户ID查询用户信息
     * @author zifangsky
     * @date 2017/11/2 10:48
     * @since 1.0.0
     * @param userId 用户ID
     * @return cn.zifangsky.quickmodules.user.model.SysUser
     */
    SysUser selectByUserId(Long userId);

    /**
     * 通过用户名查询用户基本信息+角色信息+权限信息
     * @author zifangsky
     * @date 2017/11/5 13:11
     * @since 1.0.0
     * @param username 用户名
     * @return cn.zifangsky.quickmodules.user.model.bo.SysUserBo
     */
    SysUser selectByUsername(String username);

    /**
     * 通过用户名查询用户基本信息+角色信息+权限信息
     * @author zifangsky
     * @date 2017/11/6 15:43
     * @since 1.0.0
     * @param phone 手机号
     * @return cn.zifangsky.quickmodules.user.model.bo.SysUserBo
     */
    SysUser selectByPhone(String phone);

    /**
     * 通过用户ID查询用户角色信息
     * @author zifangsky
     * @date 2017/11/2 10:48
     * @since 1.0.0
     * @param userId 用户ID
     * @return cn.zifangsky.quickmodules.user.model.SysRole
     */
    SysRole selectRoleByUserId(Long userId);

    /**
     * 通过用户ID查询角色、权限信息
     * @author zifangsky
     * @date 2017/12/6 14:33
     * @since 1.0.0
     * @param userId 用户ID
     * @return java.util.Set<cn.zifangsky.quickmodules.user.model.bo.SysRoleBo>
     */
    Set<SysRoleBo> selectRoleBoByUserId(Long userId);

    /**
     * 更新用户状态
     * @author zifangsky
     * @date 2017/11/15 16:25
     * @since 1.0.0
     * @param user user
     * @return java.lang.Integer
     */
    Integer updateUser(SysUser user);

    /**
     * 新增用户
     * @author zifangsky
     * @date 2017/11/15 16:25
     * @since 1.0.0
     * @param user user
     * @param roleId 角色ID
     * @return java.lang.Integer
     */
    Integer addOrUpdateUser(SysUser user, Long roleId);

    /**
     * 删除用户
     * @author zifangsky
     * @date 2017/11/15 19:18
     * @since 1.0.0
     * @param userId 用户ID
     * @return java.lang.Integer
     */
    Integer deleteUser(Long userId);

    /**
     * 分页查询
     * @author zifangsky
     * @date 2017/11/15 16:19
     * @since 1.0.0
     * @param queryParam 查询参数
     * @param pageInfoHolder 分页参数的包装类
     * @return java.util.List<cn.zifangsky.quickmodules.user.model.SysUser>
     */
    List<SysUserRoleBo> findAllUsers(String queryParam, Holder<PageInfo> pageInfoHolder);

    /**
     * 通过角色名查询角色信息
     * @author zifangsky
     * @date 2017/11/19 13:52
     * @since 1.0.0
     * @param roleName 角色名
     * @return cn.zifangsky.quickmodules.user.model.SysRole
     */
    SysRole selectByRoleName(String roleName);

    /**
     * 通过角色ID查询角色信息
     * @author zifangsky
     * @date 2017/11/19 13:52
     * @since 1.0.0
     * @param roleId 角色ID
     * @return cn.zifangsky.quickmodules.user.model.SysRole
     */
    SysRole selectByRoleId(Long roleId);

    /**
     * 新增/修改角色信息
     * @author zifangsky
     * @date 2017/11/19 14:11
     * @since 1.0.0
     * @param role 角色信息
     * @param funcIds 该角色对应的所有资源ID
     * @return java.lang.Integer
     */
    Integer addOrUpdateRole(SysRole role, Set<Long> funcIds);

    /**
     * 删除角色
     * @author zifangsky
     * @date 2017/11/19 14:12
     * @since 1.0.0
     * @param roleId 角色ID
     * @return java.lang.Integer
     */
    Integer deleteRole(Long roleId);

    /**
     * 分页查询
     * @author zifangsky
     * @date 2017/11/15 16:19
     * @since 1.0.0
     * @param queryParam 查询参数
     * @param pageInfoHolder 分页参数的包装类
     * @return java.util.List<cn.zifangsky.quickmodules.user.model.SysUser>
     */
    List<SysRole> findAllRoles(String queryParam, Holder<PageInfo> pageInfoHolder);

    /**
     * 通过资源ID查询角色信息
     * @author zifangsky
     * @date 2017/11/19 13:52
     * @since 1.0.0
     * @param funcId 资源ID
     * @return cn.zifangsky.quickmodules.user.model.SysRole
     */
    SysFunction selectByFuncId(Long funcId);

    /**
     * 通过资源名称和parentId查询
     * @author zifangsky
     * @date 2017/11/19 16:36
     * @since 1.0.0
     * @param name 资源名称
     * @param parentId parentId
     * @return cn.zifangsky.quickmodules.user.model.SysFunction
     */
    SysFunction selectFuncByName(String name, String parentId);

    /**
     * 分页查询资源
     * @author zifangsky
     * @date 2017/11/19 16:37
     * @since 1.0.0
     * @return java.util.List<cn.zifangsky.quickmodules.user.model.SysFunction>
     */
    List<SysFunction> findAllFunc(String queryParam, Holder<PageInfo> pageInfoHolder);

    /**
     * 通过角色ID查询资源列表
     * @author zifangsky
     * @date 2017/11/19 16:37
     * @since 1.0.0
     * @return java.util.Set<cn.zifangsky.quickmodules.user.model.SysFunction>
     */
    Set<SysFunction> selectFuncsByRoleId(Long roleId);

    /**
     * 查询所有
     * @author zifangsky
     * @date 2017/11/19 16:37
     * @since 1.0.0
     * @return java.util.List<cn.zifangsky.quickmodules.user.model.SysFunction>
     */
    List<SysFunction> findAllFunc();

    /**
     * 新增/修改资源信息
     * @author zifangsky
     * @date 2017/11/19 16:43
     * @since 1.0.0
     * @param func 资源信息
     * @return java.lang.Integer
     */
    Integer addOrUpdateFunc(SysFunction func);

    /**
     * 删除资源
     * @author zifangsky
     * @date 2017/11/19 16:43
     * @since 1.0.0
     * @param funcId 资源ID
     * @return java.lang.Integer
     */
    Integer deleteFunc(Long funcId);

    /**
     * 注册
     * @author zifangsky
     * @date 2017/11/2 10:48
     * @since 1.0.0
     * @param user 用户详情
     * @return boolean
     */
    boolean register(SysUser user);

    /**
     * 生成图片验证码
     * @author zifangsky
     * @date 2017/11/12 14:06
     * @since 1.0.0
     */
    public void generateAuthImage(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 校验图片验证码
     * @author zifangsky
     * @date 2017/11/12 14:41
     * @since 1.0.0
     * @return boolean
     */
    public boolean checkLoginVerifyCode(HttpServletRequest request);

    /**
     * 发送登录时的短信验证码
     * @author zifangsky
     * @date 2017/11/12 16:01
     * @since 1.0.0
     * @param phone 手机号码
     */
    public void sendLoginPhoneVerifyCode(String phone, HttpServletRequest request) throws Exception;

}
