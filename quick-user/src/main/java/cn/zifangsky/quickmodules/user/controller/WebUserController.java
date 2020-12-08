package cn.zifangsky.quickmodules.user.controller;

import cn.zifangsky.quickmodules.common.common.Holder;
import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.common.common.resp.BaseResultCode;
import cn.zifangsky.quickmodules.common.common.resp.Result;
import cn.zifangsky.quickmodules.common.common.resp.ResultUtils;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户相关controller
 *
 * @author zifangsky
 * @date 2020/11/17
 * @since 1.1.0
 */
@Slf4j
@Controller
public class WebUserController {

    private UserService userService;

    public WebUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询当前用户的基本信息
     * @author zifangsky
     * @date 2018/12/27 16:20
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping(value = "/user/selectUserInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> selectUserInfo(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/user/selectUserInfo」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        try {
            HttpSession session = request.getSession();
            SysUser user = (SysUser) session.getAttribute(Constants.SESSION_USER);
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);

            dataMap.put("user_info", userInfo);
            return ResultUtils.success(tid, dataMap);
        }catch (Exception e){
            log.error("查询当前用户的基本信息出现异常：", e);
            return ResultUtils.error(tid);
        }
    }

    /**
     * 查询当前用户拥有的所有角色和资源
     * @author zifangsky
     * @date 2018/12/27 16:20
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping(value = "/user/selectRoleAndFunc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> selectRoleAndFunc(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/user/selectRoleAndFunc」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

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

            dataMap.put("roles", roles);
            dataMap.put("funcs", funcs);
        }catch (Exception e){
            log.error("根据条件查询当前用户拥有的所有角色和资源出现异常：", e);
            return ResultUtils.error(tid);
        }
        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 分页查询
     * @author zifangsky
     * @date 2018/11/15 18:46
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> findAllUsers(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/user/findAll」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

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
                dataMap.put("list", list);
                dataMap.put("pageInfo", pageInfo);
                return ResultUtils.success(tid, dataMap);
            }else{
                return ResultUtils.error(BaseResultCode.DATA_NO_RESULTS, tid);
            }
        }catch (Exception e){
            log.error("根据条件查询用户列表出现异常：", e);
            return ResultUtils.error(tid);
        }
    }

    /**
     * 检查用户名是否重复
     * @author zifangsky
     * @date 2018/11/15 19:15
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/checkName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object>checkUserName(HttpServletRequest request) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/user/checkName」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        //用户名
        String username = request.getParameter("username");

        try {
            if(StringUtils.isBlank(username)){
                dataMap.put("msg","请求参数「username」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                SysUser sysUser = userService.selectByUsername(username);

                if(sysUser != null){
                    dataMap.put("valid","false");
                }else{
                    dataMap.put("valid","true");
                }
            }
        }catch (Exception e){
            log.error("检查用户名是否重复过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 检查手机号是否重复
     * @author zifangsky
     * @date 2018/11/15 19:15
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/checkPhone", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> checkUserPhone(HttpServletRequest request) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/user/checkPhone」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        //用户名
        String phone = request.getParameter("phone");

        try {
            if(StringUtils.isBlank(phone)){
                dataMap.put("msg","请求参数「phone」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                SysUser sysUser = userService.selectByPhone(phone);

                if(sysUser != null){
                    dataMap.put("valid","false");
                }else{
                    dataMap.put("valid","true");
                }
            }
        }catch (Exception e){
            log.error("检查手机号是否重复过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 新增/编辑用户信息
     * @author zifangsky
     * @date 2018/11/15 19:15
     * @since 1.0.0
     * @param user user
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/addOrEdit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> addOrEditUser(SysUser user, @RequestParam("roleId") Long roleId) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/user/addOrEdit」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        try {
            if(user == null || StringUtils.isBlank(user.getUsername())
                    || StringUtils.isBlank(user.getPassword()) ||StringUtils.isBlank(user.getPhone())){
                dataMap.put("msg","请求参数「username」或者「password」或者「phone」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                Integer addResult = userService.addOrUpdateUser(user, roleId);

                if(addResult == 1){
                    dataMap.put("code","true");
                }else {
                    dataMap.put("code","false");
                }
            }
        }catch (Exception e){
            log.error("新增过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 删除用户（逻辑删除）
     * @author zifangsky
     * @date 2018/11/15 19:15
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/user/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> deleteUser(HttpServletRequest request) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/user/delete」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        //用户ID
        String id = request.getParameter("id");

        try {
            if(StringUtils.isBlank(id)){
                dataMap.put("msg","请求参数「id」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                Integer deleteResult = userService.deleteUser(Long.valueOf(id));

                if(deleteResult == 1){
                    dataMap.put("result","true");
                }else {
                    dataMap.put("result","false");
                }
            }
        }catch (Exception e){
            log.error("删除用户过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 分页查询
     * @author zifangsky
     * @date 2018/11/19 14:45
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/role/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> findAllRoles(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/role/findAll」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

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
                dataMap.put("list", list);
                dataMap.put("pageInfo", pageInfo);
                return ResultUtils.success(tid, dataMap);
            }else{
                return ResultUtils.error(BaseResultCode.DATA_NO_RESULTS, tid);
            }
        }catch (Exception e){
            log.error("根据条件查询角色列表出现异常：", e);
            return ResultUtils.error(tid);
        }
    }

    /**
     * 检查角色名是否重复
     * @author zifangsky
     * @date 2018/11/19 14:45
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/role/checkName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> checkRoleName(HttpServletRequest request) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/role/checkName」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        //角色名
        String roleName = request.getParameter("roleName");

        try {
            if(StringUtils.isBlank(roleName)){
                dataMap.put("msg", "请求参数「roleName」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                SysRole role = userService.selectByRoleName(roleName);

                if(role != null){
                    dataMap.put("valid","false");
                }else{
                    dataMap.put("valid","true");
                }
            }
        }catch (Exception e){
            log.error("检查角色名是否重复过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 新增/编辑角色信息
     * @author zifangsky
     * @date 2018/11/19 14:45
     * @since 1.0.0
     * @param role role
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/role/addOrEdit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> addOrEditRole(SysRole role, @RequestParam("funcIdStr") String funcIdStr) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/role/addOrEdit」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        try {
            if(role == null || StringUtils.isBlank(role.getName())
                    || StringUtils.isBlank(funcIdStr)){
                dataMap.put("msg", "请求参数「name」或者「funcIdStr」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                String[] funcIdsArray = funcIdStr.split(",");
                Set<Long> funcIds = Arrays.asList(funcIdsArray).parallelStream()
                        .map(Long::valueOf)
                        .collect(Collectors.toSet());

                Integer addResult = userService.addOrUpdateRole(role, funcIds);

                if(addResult == 1){
                    dataMap.put("result","true");
                }else {
                    dataMap.put("result","false");
                }
            }
        }catch (Exception e){
            log.error("新增过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 删除角色
     * @author zifangsky
     * @date 2018/11/19 14:45
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/role/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> deleteRole(HttpServletRequest request) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/role/delete」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        //角色ID
        String id = request.getParameter("id");

        try {
            if(StringUtils.isBlank(id)){
                dataMap.put("msg", "请求参数「id」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                Integer deleteResult = userService.deleteRole(Long.valueOf(id));

                if(deleteResult == 1){
                    dataMap.put("result","true");
                }else {
                    dataMap.put("result","false");
                }
            }
        }catch (Exception e){
            log.error("删除角色过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 查询所有资源（分页）
     * @author zifangsky
     * @date 2018/11/19 17:40
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> findAllFuncs(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/func/findAll」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

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
                dataMap.put("list", list);
                dataMap.put("pageInfo", pageInfo);
                return ResultUtils.success(tid, dataMap);
            }else{
                return ResultUtils.error(BaseResultCode.DATA_NO_RESULTS, tid);
            }
        }catch (Exception e){
            log.error("根据条件查询资源列表出现异常：", e);
            return ResultUtils.error(tid);
        }
    }

    /**
     * 查询所有资源（不分页）
     * @author zifangsky
     * @date 2018/11/19 17:40
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/selectAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> selectAllFuncs(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/func/selectAll」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        try {
            List<SysFunction> list = userService.findAllFunc();

            if(list != null){
                dataMap.put("list", list);
                return ResultUtils.success(tid, dataMap);
            }else{
                return ResultUtils.error(BaseResultCode.DATA_NO_RESULTS, tid);
            }
        }catch (Exception e){
            log.error("根据条件查询资源列表出现异常：", e);
            return ResultUtils.error(tid);
        }
    }

    /**
     * 检查资源名是否重复
     * @author zifangsky
     * @date 2018/11/19 17:28
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/checkName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> checkFuncName(HttpServletRequest request) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/func/checkName」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        //资源名
        String name = request.getParameter("name");
        //parentId
        String parentId = request.getParameter("parentId");

        try {
            if(StringUtils.isBlank(name) || StringUtils.isBlank(parentId)){
                dataMap.put("msg", "请求参数「name」或者「parentId」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                SysFunction func = userService.selectFuncByName(name, parentId);

                if(func != null){
                    dataMap.put("valid","false");
                }else{
                    dataMap.put("valid","true");
                }
            }
        }catch (Exception e){
            log.error("检查资源名是否重复过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 新增/编辑资源信息
     * @author zifangsky
     * @date 2018/11/19 17:28
     * @since 1.0.0
     * @param func func
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/addOrEdit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> addOrEditFunc(SysFunction func) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/func/addOrEdit」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        try {
            if(func == null || StringUtils.isBlank(func.getName())
                    || StringUtils.isBlank(func.getParentId()) || StringUtils.isBlank(func.getPathUrl())
                    || func.getType() == null){
                dataMap.put("msg", "请求参数「name」或者「parentId」或者「pathUrl」或者「type」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                Integer addResult = userService.addOrUpdateFunc(func);

                if(addResult == 1){
                    dataMap.put("result","true");
                }else {
                    dataMap.put("result","false");
                }
            }
        }catch (Exception e){
            log.error("新增过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

    /**
     * 删除资源
     * @author zifangsky
     * @date 2018/11/19 17:28
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/func/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> deleteFunc(HttpServletRequest request) {
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/func/delete」接口，tid=[%s]", tid));
        Map<String,Object> dataMap = new HashMap<>(4);

        //角色ID
        String id = request.getParameter("id");

        try {
            if(StringUtils.isBlank(id)){
                dataMap.put("msg", "请求参数「id」不能为空！");
                return ResultUtils.error(BaseResultCode.PARAM_TYPE_BIND_ERROR, tid, dataMap);
            }else{
                Integer deleteResult = userService.deleteFunc(Long.valueOf(id));

                if(deleteResult == 1){
                    dataMap.put("result","true");
                }else {
                    dataMap.put("result","false");
                }
            }
        }catch (Exception e){
            log.error("删除资源过程中出现异常：", e);
            return ResultUtils.error(tid);
        }

        return ResultUtils.success(tid, dataMap);
    }

}
