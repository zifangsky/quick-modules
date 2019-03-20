package cn.zifangsky.quickmodules.user.controller;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.common.enums.SortOrderTypes;
import cn.zifangsky.quickmodules.common.utils.BeanUtils;
import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.model.SysFunction;
import cn.zifangsky.quickmodules.user.model.SysRole;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.model.bo.SysRoleBo;
import cn.zifangsky.quickmodules.user.model.bo.SysUserRoleBo;
import cn.zifangsky.quickmodules.user.model.vo.UserInfo;
import cn.zifangsky.quickmodules.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Holder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户相关controller
 *
 * @author zifangsky
 * @date 2017/11/2
 * @since 1.0.0
 */
@Controller
public class WebUserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;

    public WebUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询当前用户的基本信息
     * @author zifangsky
     * @date 2017/12/27 16:20
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping(value = "/user/selectUserInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> selectUserInfo(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(2);

        try {
            HttpSession session = request.getSession();
            SysUser user = (SysUser) session.getAttribute(Constants.SESSION_USER);
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);

            result.put("code", 200);
            result.put("userInfo", userInfo);
        }catch (Exception e){
            logger.error("查询当前用户的基本信息出现异常：", e);
            result.put("code", 500);
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }
        return result;
    }

    /**
     * 查询当前用户拥有的所有角色和资源
     * @author zifangsky
     * @date 2017/12/27 16:20
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping(value = "/user/selectRoleAndFunc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> selectRoleAndFunc(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(2);

        try {
            HttpSession session = request.getSession();
            SysUser user = (SysUser) session.getAttribute(Constants.SESSION_USER);
            Set<SysRole> roles = new HashSet<>(10);
            Set<SysFunction> funcs = new HashSet<>(20);

            //通过用户ID查询角色权限信息
            Set<SysRoleBo> list = userService.selectRoleBoByUserId(user.getId());
            if(list != null){
                list.forEach(roleBo -> {
                    SysRole role = new SysRole();
                    try {
                        BeanUtils.copyProperties(roleBo, role);
                    } catch (Exception e) {
                        //ignore
                    }
                    roles.add(role);

                    Set<SysFunction> temp = roleBo.getFuncs();
                    if(temp != null && temp.size() > 0){
                        funcs.addAll(temp);
                    }
                });
            }

            result.put("code", 200);
            result.put("roles", roles);
            result.put("funcs", funcs);
        }catch (Exception e){
            logger.error("根据条件查询当前用户拥有的所有角色和资源出现异常：", e);
            result.put("code", 500);
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }
        return result;
    }

    /**
     * 分页查询
     * @author zifangsky
     * @date 2017/11/15 18:46
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> findAllUsers(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(4);

        //当前页
        String currentPage = request.getParameter("current_page");
        //每页展示数
        String pageSize = request.getParameter("page_size");
        //排序字段
        String sortName = request.getParameter("sort_name");
        //排序顺序
        String sortOrder = request.getParameter("sort_order");
        //查询条件
        String queryParam = request.getParameter("query_param");

        try {
            PageInfo pageInfo = new PageInfo();
            if(StringUtils.isNoneBlank(currentPage)){
                pageInfo.setCurrentPage(Integer.valueOf(currentPage));
            }
            if(StringUtils.isNoneBlank(pageSize) && Integer.valueOf(pageSize) > 0){
                pageInfo.setPageSize(Integer.valueOf(pageSize));
            }
            if(StringUtils.isNoneBlank(sortName)){
                pageInfo.setSortName(sortName);
            }
            //查找排序方式
            SortOrderTypes sortOrderType = SortOrderTypes.fromCode(sortOrder);
            if(sortOrderType != null){
                pageInfo.setSortOrder(sortOrderType.getCode());
            }

            Holder<PageInfo> pageInfoHolder = new Holder<>(pageInfo);
            List<SysUserRoleBo> list = userService.findAllUsers(queryParam, pageInfoHolder);

            if(list != null){
                result.put("code", 200);
                result.put("list", list);
                result.put("pageInfo", pageInfo);
            }else{
                result.put("code", 500);
                result.put("msg","查询出现异常，具体原因请查看日志！");
            }
        }catch (Exception e){
            logger.error("根据条件查询用户列表出现异常：", e);
            result.put("code", 500);
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }
        return result;
    }

    /**
     * 检查用户名是否重复
     * @author zifangsky
     * @date 2017/11/15 19:15
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/checkName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> checkUserName(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);

        //用户名
        String username = request.getParameter("username");

        try {
            if(StringUtils.isBlank(username)){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                SysUser sysUser = userService.selectByUsername(username);

                if(sysUser != null){
                    result.put("code","false");
                }else{
                    result.put("code","true");
                }
            }
        }catch (Exception e){
            logger.error("检查用户名是否重复过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 检查手机号是否重复
     * @author zifangsky
     * @date 2017/11/15 19:15
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/checkPhone", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> checkUserPhone(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);

        //用户名
        String phone = request.getParameter("phone");

        try {
            if(StringUtils.isBlank(phone)){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                SysUser sysUser = userService.selectByPhone(phone);

                if(sysUser != null){
                    result.put("code","false");
                }else{
                    result.put("code","true");
                }
            }
        }catch (Exception e){
            logger.error("检查手机号是否重复过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 新增/编辑用户信息
     * @author zifangsky
     * @date 2017/11/15 19:15
     * @since 1.0.0
     * @param user user
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/addOrEdit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> addOrEditUser(SysUser user, @RequestParam("roleId") Long roleId) {
        Map<String, Object> result = new HashMap<>(2);

        try {
            if(user == null || StringUtils.isBlank(user.getUsername())
                    || StringUtils.isBlank(user.getPassword()) ||StringUtils.isBlank(user.getPhone())){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                Integer addResult = userService.addOrUpdateUser(user, roleId);

                if(addResult == 1){
                    result.put("code","true");
                }else {
                    result.put("code","false");
                }
            }
        }catch (Exception e){
            logger.error("新增过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","新增用户出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 删除用户（逻辑删除）
     * @author zifangsky
     * @date 2017/11/15 19:15
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> deleteUser(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);

        //用户ID
        String id = request.getParameter("id");

        try {
            if(StringUtils.isBlank(id)){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                Integer deleteResult = userService.deleteUser(Long.valueOf(id));

                if(deleteResult == 1){
                    result.put("code","true");
                }else {
                    result.put("code","false");
                }
            }
        }catch (Exception e){
            logger.error("删除用户过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","删除用户出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 分页查询
     * @author zifangsky
     * @date 2017/11/19 14:45
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/role/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> findAllRoles(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(4);

        //当前页
        String currentPage = request.getParameter("current_page");
        //每页展示数
        String pageSize = request.getParameter("page_size");
        //排序字段
        String sortName = request.getParameter("sort_name");
        //排序字段
        String sortOrder = request.getParameter("sort_order");
        //查询条件
        String queryParam = request.getParameter("query_param");

        try {
            PageInfo pageInfo = new PageInfo();
            if(StringUtils.isNoneBlank(currentPage)){
                pageInfo.setCurrentPage(Integer.valueOf(currentPage));
            }
            if(StringUtils.isNoneBlank(pageSize)){
                pageInfo.setPageSize(Integer.valueOf(pageSize));
            }
            if(StringUtils.isNoneBlank(sortName)){
                pageInfo.setSortName(sortName);
            }
            //查找排序方式
            SortOrderTypes sortOrderType = SortOrderTypes.fromCode(sortOrder);
            if(sortOrderType != null){
                pageInfo.setSortOrder(sortOrderType.getCode());
            }

            Holder<PageInfo> pageInfoHolder = new Holder<>(pageInfo);
            List<SysRole> list = userService.findAllRoles(queryParam, pageInfoHolder);

            if(list != null){
                result.put("code", 200);
                result.put("list", list);
                result.put("pageInfo", pageInfo);
            }else{
                result.put("code", 500);
                result.put("msg","查询出现异常，具体原因请查看日志！");
            }
        }catch (Exception e){
            logger.error("根据条件查询角色列表出现异常：", e);
            result.put("code", 500);
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }
        return result;
    }

    /**
     * 检查角色名是否重复
     * @author zifangsky
     * @date 2017/11/19 14:45
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/role/checkName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> checkRoleName(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);

        //角色名
        String roleName = request.getParameter("roleName");

        try {
            if(StringUtils.isBlank(roleName)){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                SysRole role = userService.selectByRoleName(roleName);

                if(role != null){
                    result.put("code","false");
                }else{
                    result.put("code","true");
                }
            }
        }catch (Exception e){
            logger.error("检查角色名是否重复过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 新增/编辑角色信息
     * @author zifangsky
     * @date 2017/11/19 14:45
     * @since 1.0.0
     * @param role role
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/role/addOrEdit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> addOrEditRole(SysRole role, @RequestParam("funcIdStr") String funcIdStr) {
        Map<String, Object> result = new HashMap<>(2);

        try {
            if(role == null || StringUtils.isBlank(role.getName())
                    || StringUtils.isBlank(funcIdStr)){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                String[] funcIdsArray = funcIdStr.split(",");
                Set<Long> funcIds = Arrays.asList(funcIdsArray).parallelStream()
                        .map(Long::valueOf)
                        .collect(Collectors.toSet());

                Integer addResult = userService.addOrUpdateRole(role, funcIds);

                if(addResult == 1){
                    result.put("code","true");
                }else {
                    result.put("code","false");
                }
            }
        }catch (Exception e){
            logger.error("新增过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","新增角色出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 删除角色
     * @author zifangsky
     * @date 2017/11/19 14:45
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/role/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> deleteRole(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);

        //角色ID
        String id = request.getParameter("id");

        try {
            if(StringUtils.isBlank(id)){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                Integer deleteResult = userService.deleteRole(Long.valueOf(id));

                if(deleteResult == 1){
                    result.put("code","true");
                }else {
                    result.put("code","false");
                }
            }
        }catch (Exception e){
            logger.error("删除角色过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","删除角色出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 查询所有资源（分页）
     * @author zifangsky
     * @date 2017/11/19 17:40
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> findAllFuncs(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(4);

        //当前页
        String currentPage = request.getParameter("current_page");
        //每页展示数
        String pageSize = request.getParameter("page_size");
        //排序字段
        String sortName = request.getParameter("sort_name");
        //排序字段
        String sortOrder = request.getParameter("sort_order");
        //查询条件
        String queryParam = request.getParameter("query_param");

        try {
            PageInfo pageInfo = new PageInfo();
            if(StringUtils.isNoneBlank(currentPage)){
                pageInfo.setCurrentPage(Integer.valueOf(currentPage));
            }
            if(StringUtils.isNoneBlank(pageSize)){
                pageInfo.setPageSize(Integer.valueOf(pageSize));
            }
            if(StringUtils.isNoneBlank(sortName)){
                pageInfo.setSortName(sortName);
            }
            //查找排序方式
            SortOrderTypes sortOrderType = SortOrderTypes.fromCode(sortOrder);
            if(sortOrderType != null){
                pageInfo.setSortOrder(sortOrderType.getCode());
            }

            Holder<PageInfo> pageInfoHolder = new Holder<>(pageInfo);
            List<SysFunction> list = userService.findAllFunc(queryParam, pageInfoHolder);

            if(list != null){
                result.put("code", 200);
                result.put("list", list);
                result.put("pageInfo", pageInfo);
            }else{
                result.put("code", 500);
                result.put("msg","查询出现异常，具体原因请查看日志！");
            }
        }catch (Exception e){
            logger.error("根据条件查询资源列表出现异常：", e);
            result.put("code", 500);
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }
        return result;
    }

    /**
     * 查询所有资源（不分页）
     * @author zifangsky
     * @date 2017/11/19 17:40
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/selectAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> selectAllFuncs(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>(4);

        try {
            List<SysFunction> list = userService.findAllFunc();

            if(list != null){
                result.put("code", 200);
                result.put("list", list);
            }else{
                result.put("code", 500);
                result.put("msg","查询出现异常，具体原因请查看日志！");
            }
        }catch (Exception e){
            logger.error("根据条件查询资源列表出现异常：", e);
            result.put("code", 500);
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }
        return result;
    }

    /**
     * 检查资源名是否重复
     * @author zifangsky
     * @date 2017/11/19 17:28
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/checkName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> checkFuncName(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);

        //资源名
        String name = request.getParameter("name");
        //parentId
        String parentId = request.getParameter("parentId");

        try {
            if(StringUtils.isBlank(name) || StringUtils.isBlank(parentId)){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                SysFunction func = userService.selectFuncByName(name, parentId);

                if(func != null){
                    result.put("code","false");
                }else{
                    result.put("code","true");
                }
            }
        }catch (Exception e){
            logger.error("检查资源名是否重复过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 新增/编辑资源信息
     * @author zifangsky
     * @date 2017/11/19 17:28
     * @since 1.0.0
     * @param func func
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/addOrEdit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> addOrEditFunc(SysFunction func) {
        Map<String, Object> result = new HashMap<>(2);

        try {
            if(func == null || StringUtils.isBlank(func.getName())
                    || StringUtils.isBlank(func.getParentId()) || StringUtils.isBlank(func.getPathUrl())
                    || func.getType() == null){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                Integer addResult = userService.addOrUpdateFunc(func);

                if(addResult == 1){
                    result.put("code","true");
                }else {
                    result.put("code","false");
                }
            }
        }catch (Exception e){
            logger.error("新增过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","新增资源出现异常，具体原因请查看日志！");
        }

        return result;
    }

    /**
     * 删除资源
     * @author zifangsky
     * @date 2017/11/19 17:28
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> deleteFunc(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);

        //角色ID
        String id = request.getParameter("id");

        try {
            if(StringUtils.isBlank(id)){
                result.put("msg","请求参数不能为空！");
                return result;
            }else{
                Integer deleteResult = userService.deleteFunc(Long.valueOf(id));

                if(deleteResult == 1){
                    result.put("code","true");
                }else {
                    result.put("code","false");
                }
            }
        }catch (Exception e){
            logger.error("删除资源过程中出现异常：", e);
            result.put("code","false");
            result.put("msg","删除资源出现异常，具体原因请查看日志！");
        }

        return result;
    }

}
