package cn.zifangsky.quickmodules.user.mapper;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysRole;

import javax.xml.ws.Holder;
import java.util.List;
import java.util.Set;

public interface RoleMapper {
    int deleteLogically(Long id);

    int delete(Long id);

    int insert(SysRole role);

    int insertSelective(SysRole role);

    SysRole selectByPrimaryKey(Long id);

    SysRole selectByUserId(Long userId);

    /**
     * 通过角色Id查询该角色对应的所有权限信息
     * @author zifangsky
     * @date 2017/11/5 14:33
     * @since 1.0.0
     * @param role 角色Id
     * @return java.util.Set<cn.zifangsky.quickmodules.user.model.SysFunction>
     */
    Set<SysFunction> selectFuncsByRoleId(Long role);

    int updateByPrimaryKeySelective(SysRole role);

    int updateByPrimaryKey(SysRole role);

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
     * 分页查询
     * @author zifangsky
     * @date 2017/11/19 13:38
     * @since 1.0.0
     * @param queryParam 查询参数
     * @param pageInfoHolder 分页参数的包装类
     * @return java.util.List<cn.zifangsky.quickmodules.user.model.SysRole>
     */
    List<SysRole> findAll(String queryParam, Holder<PageInfo> pageInfoHolder);

    /**
     * 添加角色资源信息
     * @author zifangsky
     * @date 2017/11/19 14:05
     * @since 1.0.0
     * @param funcIds 资源ID列表
     * @param roleId 角色ID
     */
    void addRolePermission(Long roleId, Set<Long> funcIds);

    /**
     * 物理删除角色资源信息
     * @author zifangsky
     * @date 2017/11/19 14:34
     * @since 1.0.0
     * @param id 角色资源ID
     * @return int
     */
    int deleteRolePermission(Long id);

    /**
     * 更新角色资源信息
     * @author zifangsky
     * @date 2017/11/19 14:05
     * @since 1.0.0
     * @param funcIds 资源ID列表
     * @param roleId 角色ID
     */
    void updateRolePermission(Long roleId, Set<Long> funcIds);
}