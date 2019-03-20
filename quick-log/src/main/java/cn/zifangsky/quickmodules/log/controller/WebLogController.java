package cn.zifangsky.quickmodules.log.controller;

import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.common.enums.SortOrderTypes;
import cn.zifangsky.quickmodules.log.model.SysLog;
import cn.zifangsky.quickmodules.log.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Holder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志相关查询接口
 *
 * @author zifangsky
 * @date 2017/12/6
 * @since 1.0.0
 */
@Controller
public class WebLogController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "logServiceImpl")
    private LogService logService;

    /**
     * 分页查询
     * @author zifangsky
     * @date 2017/12/6 09:36
     * @since 1.0.0
     * @param request request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping(value = "/log/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String,Object> findAllUsers(HttpServletRequest request){
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
        //角色ID
        String roleIdStr = request.getParameter("role_id");

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
            Long roleId = null;
            if(StringUtils.isNoneBlank(roleIdStr)){
                roleId = Long.valueOf(roleIdStr);
            }

            //查找排序方式
            SortOrderTypes sortOrderType = SortOrderTypes.fromCode(sortOrder);
            if(sortOrderType != null){
                pageInfo.setSortOrder(sortOrderType.getCode());
            }

            Holder<PageInfo> pageInfoHolder = new Holder<>(pageInfo);
            List<SysLog> list = logService.findAll(roleId, queryParam, pageInfoHolder);

            if(list != null){
                result.put("code", 200);
                result.put("list", list);
                result.put("pageInfo", pageInfo);
            }else{
                result.put("code", 500);
                result.put("msg","查询出现异常，具体原因请查看日志！");
            }
        }catch (Exception e){
            logger.error("根据条件查询日志列表出现异常：", e);
            result.put("code", 500);
            result.put("msg","查询出现异常，具体原因请查看日志！");
        }
        return result;
    }

}
