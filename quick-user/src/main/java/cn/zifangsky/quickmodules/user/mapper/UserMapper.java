package cn.zifangsky.quickmodules.user.mapper;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.model.bo.SysRoleBo;

import javax.xml.ws.Holder;
import java.util.List;
import java.util.Set;

public interface UserMapper {
    Integer deleteLogically(Long id);

    Integer insert(SysUser user);

    Integer insertSelective(SysUser user);

    SysUser selectByPrimaryKey(Long id);

    /**
     * 通过用户名查询用户信息
     * @author zifangsky
     * @date 2017/11/5 14:31
     * @since 1.0.0
     * @param username 用户名
     * @return cn.zifangsky.quickmodules.user.model.SysUser
     */
    SysUser selectByUsername(String username);

    /**
     * 通过用户名查询用户基本信息+角色信息+权限信息
     * @author zifangsky
     * @date 2017/11/6 15:43
     * @since 1.0.0
     * @param phone 手机号
     * @return cn.zifangsky.quickmodules.user.model.SysUser
     */
    SysUser selectByPhone(String phone);

    /**
     * 通过用户ID查询该用户的所有的角色信息
     * @author zifangsky
     * @date 2017/11/5 14:21
     * @since 1.0.0
     * @param userId 用户ID
     * @return java.util.Set<cn.zifangsky.quickmodules.user.model.bo.SysRoleBo>
     */
    Set<SysRoleBo> selectRolesByUserId(Long userId);

    Integer updateByPrimaryKeySelective(SysUser user);

    Integer updateByPrimaryKey(SysUser user);

    /**
     * 分页查询
     * @author zifangsky
     * @date 2017/11/15 16:13
     * @since 1.0.0
     * @param queryParam 查询参数
     * @param pageInfoHolder 分页参数的包装类
     * @return java.util.List<cn.zifangsky.quickmodules.user.model.SysUser>
     */
    List<SysUser> findAll(String queryParam, Holder<PageInfo> pageInfoHolder);

    /**
     * 添加用户角色信息
     * @author zifangsky
     * @date 2017/11/15 16:44
     * @since 1.0.0
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    Integer addUserRole(Long userId, Long roleId);

    /**
     * 通过用户ID更新对应的角色信息
     * <p><b>Note:</b> 适用于一个用户只属于一种角色</p>
     * @author zifangsky
     * @date 2017/11/19 11:35
     * @since 1.0.0
     * @param userId 用户ID
     * @param newRoleId 新的角色ID
     * @return java.lang.Integer
     */
    Integer updateUserRole(Long userId, Long newRoleId);
}