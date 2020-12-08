package cn.zifangsky.quickmodules.user.model;

import lombok.Data;

/**
 * sys_user_role
 */
@Data
public class SysUserRole{
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;
}