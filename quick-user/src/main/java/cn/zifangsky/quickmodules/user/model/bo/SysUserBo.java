package cn.zifangsky.quickmodules.user.model.bo;

import cn.zifangsky.quickmodules.user.model.SysUser;

import java.util.Set;

/**
 * 扩展用户类
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public class SysUserBo extends SysUser {
    /**
     * 用户所属的角色信息
     */
    private Set<SysRoleBo> roles;

    public Set<SysRoleBo> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRoleBo> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "SysUserBo{" +
                "roles=" + roles +
                '}';
    }
}
