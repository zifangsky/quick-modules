package cn.zifangsky.quickmodules.user.service.impl;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.common.common.SpringContextUtils;
import cn.zifangsky.quickmodules.common.utils.BeanUtils;
import cn.zifangsky.quickmodules.common.utils.EncryptUtils;
import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.mapper.FunctionMapper;
import cn.zifangsky.quickmodules.user.mapper.RoleMapper;
import cn.zifangsky.quickmodules.user.mapper.UserMapper;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysRole;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.model.bo.SysRoleBo;
import cn.zifangsky.quickmodules.user.model.bo.SysUserRoleBo;
import cn.zifangsky.quickmodules.user.plugins.PluginManager;
import cn.zifangsky.quickmodules.user.plugins.WebUserInfo;
import cn.zifangsky.quickmodules.user.service.UserService;
import cn.zifangsky.quickmodules.user.utils.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Holder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户相关Service
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private WebUserInfo webUserInfo;

    @Override
    public SysUser selectByUserId(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public SysUser selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public SysUser selectByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public SysRole selectRoleByUserId(Long userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    public Set<SysRoleBo> selectRoleBoByUserId(Long userId) {
        return userMapper.selectRolesByUserId(userId);
    }

    @Override
    public Integer updateUser(SysUser user) {
        //清空Shiro缓存
        ShiroUtils.clearAuthorizationInfo();

        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    public synchronized Integer addOrUpdateUser(SysUser user, Long roleId) {
        //清空Shiro缓存
        ShiroUtils.clearAuthorizationInfo();

        //新增记录
        if(user.getId() == null){
            //加密原始密码
            user.setPassword(this.encodePassword(user.getPassword()));
            //加密方式
            user.setEncryptMode(webUserInfo.getEncryptionType().getCode());

            SysUser createUser = (SysUser) SpringContextUtils.getSession().getAttribute(Constants.SESSION_USER);
            user.setCreater(createUser.getUsername());

            Integer result = userMapper.insertSelective(user);

            //保存之后的用户信息，再次查询主要是为了获取用户ID
            SysUser saved = userMapper.selectByUsername(user.getUsername());
            if(saved != null){
                //添加用户角色信息
                userMapper.addUserRole(saved.getId(), roleId);
            }

            return result;
        }
        //修改原纪录
        else{
            //更新用户角色信息
            userMapper.updateUserRole(user.getId(), roleId);
            return userMapper.updateByPrimaryKey(user);
        }
    }

    @Override
    public synchronized Integer deleteUser(Long userId) {
        //清空Shiro缓存
        ShiroUtils.clearAuthorizationInfo();

        return userMapper.deleteLogically(userId);
    }

    @Override
    public boolean register(SysUser user) {
        //清空Shiro缓存
        ShiroUtils.clearAuthorizationInfo();

        return false;
    }

    @Override
    public List<SysUserRoleBo> findAllUsers(String queryParam, Holder<PageInfo> pageInfoHolder) {
        List<SysUserRoleBo> result = null;
        List<SysUser> list = userMapper.findAll(queryParam, pageInfoHolder);

        if(list != null){
            result = new ArrayList<>(list.size());

            for(SysUser user : list){
                SysUserRoleBo temp = new SysUserRoleBo();
                try {
                    BeanUtils.copyProperties(user, temp);

                    SysRole role = this.selectRoleByUserId(user.getId());
                    temp.setRole(role);
                    result.add(temp);
                } catch (Exception e) {
                    //ignore
                }
            }
        }

        return result;
    }

    @Override
    public SysRole selectByRoleName(String roleName) {
        return roleMapper.selectByRoleName(roleName);
    }

    @Override
    public SysRole selectByRoleId(Long roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public synchronized Integer addOrUpdateRole(SysRole role, Set<Long> funcIds) {
        //清空Shiro缓存
        ShiroUtils.clearAuthorizationInfo();

        //新增记录
        if(role.getId() == null){
            SysUser createUser = (SysUser) SpringContextUtils.getSession().getAttribute(Constants.SESSION_USER);
            role.setCreater(createUser.getUsername());

            Integer result = roleMapper.insertSelective(role);

            //保存之后的角色信息，再次查询主要是为了获取角色ID
            SysRole saved = roleMapper.selectByRoleName(role.getName());
            if(saved != null){
                //添加角色资源信息
                roleMapper.addRolePermission(saved.getId(), funcIds);
            }

            return result;
        }
        //修改原纪录
        else{
            //更新角色资源信息
            roleMapper.updateRolePermission(role.getId(), funcIds);
            return roleMapper.updateByPrimaryKey(role);
        }
    }

    @Override
    public synchronized Integer deleteRole(Long roleId) {
        //清空Shiro缓存
        ShiroUtils.clearAuthorizationInfo();

        return roleMapper.delete(roleId);
    }

    @Override
    public List<SysRole> findAllRoles(String queryParam, Holder<PageInfo> pageInfoHolder) {
        return roleMapper.findAll(queryParam, pageInfoHolder);
    }

    @Override
    public SysFunction selectByFuncId(Long funcId) {
        return functionMapper.selectByPrimaryKey(funcId.intValue());
    }

    @Override
    public SysFunction selectFuncByName(String name, String parentId) {
        return functionMapper.selectByName(name, parentId);
    }

    @Override
    public List<SysFunction> findAllFunc(String queryParam, Holder<PageInfo> pageInfoHolder) {
        return functionMapper.findAll(queryParam, pageInfoHolder);
    }

    @Override
    public Set<SysFunction> selectFuncsByRoleId(Long roleId) {
        return roleMapper.selectFuncsByRoleId(roleId);
    }

    @Override
    public List<SysFunction> findAllFunc() {
        return functionMapper.findAll();
    }

    @Override
    public Integer addOrUpdateFunc(SysFunction func) {
        //清空Shiro缓存
        ShiroUtils.clearAuthorizationInfo();

        //新增记录
        if(func.getId() == null){
            SysUser createUser = (SysUser) SpringContextUtils.getSession().getAttribute(Constants.SESSION_USER);
            func.setCreater(createUser.getUsername());

            //查询父节点
            SysFunction parent = functionMapper.selectParentByParentId(func.getParentId());
            //查询兄弟节点
            List<SysFunction> siblingList = functionMapper.selectSiblingByParentId(func.getParentId());
            //设置Level
            func.setLevel((parent.getLevel() + 1));

            //已经存在兄弟节点
            if(siblingList != null && siblingList.size() > 0){
                //获取最大一个兄弟节点的ID名称
                String siblingIdStr = siblingList.get(siblingList.size() - 1).getMyselfId();
                Integer siblingId = Integer.valueOf(siblingIdStr.replace(Constants.FUNC_ID_PREFIX, ""));
                //设置编号
                func.setMyselfId(Constants.FUNC_ID_PREFIX + String.valueOf((siblingId + 1)));
                //设置默认的排列序号
                func.setSequenceNum((siblingList.size() + 1));
            }
            //不存在兄弟节点
            else{
                //如果父节点是根节点
                if(Constants.FUNC_ID_ROOT.equals(func.getParentId())){
                    //设置编号
                    func.setMyselfId(Constants.FUNC_ID_PREFIX + "10");
                }else{
                    //设置编号
                    func.setMyselfId(func.getParentId() + "01");
                }
                //设置默认的排列序号
                func.setSequenceNum(1);
            }

            return functionMapper.insertSelective(func);
        }
        //修改原纪录
        else{
            return functionMapper.updateByPrimaryKey(func);
        }
    }

    @Override
    public Integer deleteFunc(Long funcId) {
        //清空Shiro缓存
        ShiroUtils.clearAuthorizationInfo();

        return functionMapper.delete(funcId);
    }

    @Override
    public void generateAuthImage(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PluginManager pluginManager = webUserInfo.getPluginManager();
        if(pluginManager != null){
            pluginManager.generateAuthImage(request, response,
                    webUserInfo.getVerifyImageWidth(), webUserInfo.getVerifyImageHeight());
        }
    }

    @Override
    public boolean checkLoginVerifyCode(HttpServletRequest request) {
        if(webUserInfo.getLoginVerifyCodeFlag()){
            PluginManager pluginManager = webUserInfo.getPluginManager();
            //来至Form表单中的验证码
            String formCode = request.getParameter(Constants.FORM_VERIFY_CODE);

            return pluginManager != null && pluginManager.checkVerifyCode(request, formCode);
        }else{
            return true;
        }
    }

    @Override
    public void sendLoginPhoneVerifyCode(String phone, HttpServletRequest request) throws Exception {
        PluginManager pluginManager = webUserInfo.getPluginManager();
        if(pluginManager != null){
            pluginManager.sendLoginPhoneVerifyCode(phone, request);
        }
    }

    /**
     * 将原始密码加密
     * @author zifangsky
     * @date 2017/11/15 19:45
     * @since 1.0.0
     * @param sourcePassword 原始密码
     * @return boolean
     */
    public String encodePassword(String sourcePassword){
        switch (webUserInfo.getEncryptionType()){
            case Base64:
                return EncryptUtils.base64Encode(sourcePassword);
            case Md5Hex:
                return EncryptUtils.md5Hex(sourcePassword);
            case Sha1Hex:
                return EncryptUtils.sha1Hex(sourcePassword);
            case Sha256Hex:
                return EncryptUtils.sha256Hex(sourcePassword);
            case Sha512Hex:
                return EncryptUtils.sha512Hex(sourcePassword);
            case Md5Crypt:
                return EncryptUtils.md5Crypt(sourcePassword, null);
            case Sha256Crypt:
                return EncryptUtils.sha256Crypt(sourcePassword, null);
            case Sha512Crypt:
                return EncryptUtils.sha512Crypt(sourcePassword, null);
            default:
                return null;
        }
    }

}
