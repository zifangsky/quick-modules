package cn.zifangsky.quickmodules.user.model.bo;

import cn.zifangsky.quickmodules.user.model.SysRole;
import cn.zifangsky.quickmodules.user.model.SysUser;

/**
 * 扩展用户类
 * @author zifangsky
 * @date 2017/02/20
 * @since 1.0.0
 */
public class SysUserRoleBo extends SysUser {
    /**
     * 用户所属的角色信息
     */
    private SysRole role;

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SysUserRoleBo{" +
                "role=" + role +
                '}';
    }
}
