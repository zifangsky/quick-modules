package cn.zifangsky.quickmodules.user.model;

import lombok.Data;

/**
 * sys_role_function
 */
@Data
public class SysRoleFunction{
    /**
     * 主键id
     */
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 资源id
     */
    private Long funcId;
}