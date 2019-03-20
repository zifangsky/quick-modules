package cn.zifangsky.quickmodules.user.model.bo;

import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysRole;

import java.util.Objects;
import java.util.Set;

/**
 * 扩展角色类
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
public class SysRoleBo extends SysRole {
    /**
     * 用户所属的角色信息
     */
    private Set<SysFunction> funcs;

    public Set<SysFunction> getFuncs() {
        return funcs;
    }

    public void setFuncs(Set<SysFunction> funcs) {
        this.funcs = funcs;
    }

    @Override
    public String toString() {
        return "SysRoleBo{" +
                "funcs=" + funcs +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SysRoleBo sysRoleBo = (SysRoleBo) o;
        return Objects.equals(funcs, sysRoleBo.funcs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), funcs);
    }
}
