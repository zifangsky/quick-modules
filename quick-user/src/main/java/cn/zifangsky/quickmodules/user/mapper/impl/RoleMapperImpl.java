package cn.zifangsky.quickmodules.user.mapper.impl;

import cn.zifangsky.quickmodules.common.common.Holder;
import cn.zifangsky.quickmodules.common.common.JDBCConstants;
import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.mapper.FunctionMapper;
import cn.zifangsky.quickmodules.user.mapper.RoleMapper;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysRole;
import cn.zifangsky.quickmodules.user.model.SysRoleFunction;
import cn.zifangsky.quickmodules.user.model.SysUserRole;
import cn.zifangsky.quickmodules.user.plugins.DbUtilsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色表相关的数据库操作
 *
 * @author zifangsky
 * @date 2017/11/5
 * @since 1.0.0
 */
@Repository
public class RoleMapperImpl implements RoleMapper {

    @Resource(name = "userDbUtilsTemplate")
    private DbUtilsTemplate template;

    @Autowired
    private FunctionMapper functionMapper;

    @Override
    public int deleteLogically(Long id) {
        return template.deleteLogically(Constants.TABLE_ROLE, "id", id);
    }

    @Override
    public int delete(Long id) {
        return template.delete(Constants.TABLE_ROLE, "id", id);
    }

    @Override
    public int insert(SysRole role) {
        return template.insert(Constants.TABLE_ROLE, role);
    }

    @Override
    public int insertSelective(SysRole role) {
        return template.insertSelective(Constants.TABLE_ROLE, role);
    }

    @Override
    public SysRole selectByPrimaryKey(Long id) {
        return template.select(Constants.TABLE_ROLE, "id", id, SysRole.class);
    }

    @Override
    public SysRole selectByUserId(Long userId) {
        List<SysUserRole> userRoleList = template.selectForList(Constants.TABLE_USER_ROLE, "user_id", userId, SysUserRole.class);

        if(userRoleList != null && userRoleList.size() > 0){
            //取第一条数据
            return this.selectByPrimaryKey(userRoleList.get(0).getRoleId());
        }

        return null;
    }

    @Override
    public Set<SysFunction> selectFuncsByRoleId(Long roleId) {
        //查询角色权限表
        List<SysRoleFunction> rolePermissionList = template.selectForList(Constants.TABLE_ROLE_FUNC, "role_id", roleId, SysRoleFunction.class);

        Set<SysFunction> permissions = new HashSet<>();
        if(rolePermissionList != null && rolePermissionList.size() > 0){
            for(SysRoleFunction roleFunction : rolePermissionList){
                //查询权限表
                SysFunction temp = functionMapper.selectByPrimaryKey(roleFunction.getFuncId().intValue());

                if(temp != null){
                    permissions.add(temp);
                }
            }
        }

        return permissions;
    }

    @Override
    public int updateByPrimaryKeySelective(SysRole role) {
        return template.updateSelective(Constants.TABLE_ROLE, role, "id", role.getId());
    }

    @Override
    public int updateByPrimaryKey(SysRole role) {
        return template.update(Constants.TABLE_ROLE, role, "id", role.getId());
    }

    @Override
    public SysRole selectByRoleName(String roleName) {
        return template.select(Constants.TABLE_ROLE, "name", roleName, SysRole.class);
    }

    @Override
    public List<SysRole> findAll(String queryParam, Holder<PageInfo> pageInfoHolder) {
        List<Object> valuesList = new ArrayList<>();
        if(queryParam != null){
            queryParam = queryParam.replace("%", "");
        }else{
            queryParam = "";
        }
        valuesList.add("%" + queryParam + "%");

        return template.findAll(Constants.TABLE_ROLE, JDBCConstants.ROLE_FIND_ALL, valuesList, pageInfoHolder, SysRole.class);
    }

    @Override
    public void addRolePermission(Long roleId, Set<Long> funcIds) {
        SysRoleFunction roleFunction = null;

        if(funcIds != null && funcIds.size() > 0){
            for(Long funcId : funcIds){
                roleFunction = new SysRoleFunction();
                roleFunction.setRoleId(roleId);
                roleFunction.setFuncId(funcId);

                template.insertSelective(Constants.TABLE_ROLE_FUNC, roleFunction);
            }
        }
    }

    @Override
    public int deleteRolePermission(Long id) {
        return template.delete(Constants.TABLE_ROLE_FUNC, "id", id);
    }

    @Override
    public void updateRolePermission(Long roleId, Set<Long> funcIds) {
        //查询角色权限表
        List<SysRoleFunction> roleFunctionList = template.selectForList(Constants.TABLE_ROLE_FUNC, "role_id", roleId, SysRoleFunction.class);

        //删除原来的角色资源信息
        if(roleFunctionList != null && roleFunctionList.size() > 0){
            roleFunctionList.forEach(temp -> this.deleteRolePermission(temp.getId()));
        }

        //再添加新的角色资源信息
        this.addRolePermission(roleId, funcIds);
    }
}
