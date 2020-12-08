package cn.zifangsky.quickmodules.user.mapper.impl;

import cn.zifangsky.quickmodules.common.common.Holder;
import cn.zifangsky.quickmodules.common.common.JDBCConstants;
import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.mapper.RoleMapper;
import cn.zifangsky.quickmodules.user.mapper.UserMapper;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysRole;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.model.SysUserRole;
import cn.zifangsky.quickmodules.user.model.bo.SysRoleBo;
import cn.zifangsky.quickmodules.user.plugins.DbUtilsTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户表相关的数据库操作
 *
 * @author zifangsky
 * @date 2017/11/5
 * @since 1.0.0
 */
@Repository
public class UserMapperImpl implements UserMapper {

    @Resource(name = "userDbUtilsTemplate")
    private DbUtilsTemplate template;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Integer deleteLogically(Long id) {
        return template.deleteLogically(Constants.TABLE_USER, "id", id);
    }

    @Override
    public Integer insert(SysUser user) {
        return template.insert(Constants.TABLE_USER, user);
    }

    @Override
    public Integer insertSelective(SysUser user) {
        return template.insertSelective(Constants.TABLE_USER, user);
    }

    @Override
    public SysUser selectByPrimaryKey(Long id) {
        return template.select(Constants.TABLE_USER, "id", id, SysUser.class);
    }

    @Override
    public SysUser selectByUsername(String username) {
        return template.select(Constants.TABLE_USER, "username", username, SysUser.class);
    }

    @Override
    public SysUser selectByPhone(String phone) {
        return template.select(Constants.TABLE_USER, "phone", phone, SysUser.class);
    }

    @Override
    public Set<SysRoleBo> selectRolesByUserId(Long userId) {
        //查询用户角色表
        List<SysUserRole> userRoleList = template.selectForList(Constants.TABLE_USER_ROLE, "user_id", userId, SysUserRole.class);

        Set<SysRoleBo> roles = new HashSet<>();
        if(userRoleList != null && userRoleList.size() > 0){
            for(SysUserRole userRole : userRoleList){
                //查询角色表
                SysRole temp = roleMapper.selectByPrimaryKey(userRole.getRoleId());

                if(temp != null){
                    //查询角色对应的所有权限信息
                    Set<SysFunction> func = roleMapper.selectFuncsByRoleId(userRole.getRoleId());

                    SysRoleBo sysRoleBo = new SysRoleBo();
                    BeanUtils.copyProperties(temp, sysRoleBo);

                    sysRoleBo.setFuncs(func);
                    //添加到角色Set
                    roles.add(sysRoleBo);
                }
            }
        }

        return roles;
    }

    @Override
    public Integer updateByPrimaryKeySelective(SysUser user) {
        return template.updateSelective(Constants.TABLE_USER, user, "id", user.getId());
    }

    @Override
    public Integer updateByPrimaryKey(SysUser user) {
        return template.update(Constants.TABLE_USER, user, "id", user.getId());
    }

    @Override
    public List<SysUser> findAll(String queryParam, Holder<PageInfo> pageInfoHolder) {
        List<Object> valuesList = new ArrayList<>();

        if(queryParam != null){
            queryParam = queryParam.replace("%", "");
        }else{
            queryParam = "";
        }
        valuesList.add("%" + queryParam + "%");
        valuesList.add("%" + queryParam + "%");
        valuesList.add("%" + queryParam + "%");
        valuesList.add("%" + queryParam + "%");

        return template.findAll(Constants.TABLE_USER, JDBCConstants.USER_FIND_ALL, valuesList, pageInfoHolder, SysUser.class);
    }

    @Override
    public Integer addUserRole(Long userId, Long roleId) {
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);

        return template.insertSelective(Constants.TABLE_USER_ROLE, userRole);
    }

    @Override
    public Integer updateUserRole(Long userId, Long newRoleId) {
        //查询原来的用户角色信息
        List<SysUserRole> userRoleList = template.selectForList(Constants.TABLE_USER_ROLE, "user_id", userId, SysUserRole.class);

        if(userRoleList != null && userRoleList.size() > 0){
            //取第一条数据
            SysUserRole temp = userRoleList.get(0);
            temp.setRoleId(newRoleId);

            return template.update(Constants.TABLE_USER_ROLE, temp, "id", temp.getId());
        }else {
            this.addUserRole(userId, newRoleId);
        }

        return 0;
    }
}
