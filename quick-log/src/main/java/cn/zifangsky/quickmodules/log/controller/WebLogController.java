package cn.zifangsky.quickmodules.log.controller;

import cn.zifangsky.quickmodules.common.common.Holder;
import cn.zifangsky.quickmodules.common.common.PageInfo;
import cn.zifangsky.quickmodules.common.common.resp.BaseResultCode;
import cn.zifangsky.quickmodules.common.common.resp.Result;
import cn.zifangsky.quickmodules.common.common.resp.ResultUtils;
import cn.zifangsky.quickmodules.common.enums.SortOrderTypes;
import cn.zifangsky.quickmodules.log.model.SysLog;
import cn.zifangsky.quickmodules.log.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志相关查询接口
 *
 * @author zifangsky
 * @date 2020/12/8
 * @since 1.1.0
 */
@Slf4j
@Controller
public class WebLogController {

    @Resource(name = "logServiceImpl")
    private LogService logService;

    /**
     * 分页查询
     * @author zifangsky
     * @date 2020/12/8 09:36
     * @since 1.1.0
     * @param request request
     * @return cn.zifangsky.quickmodules.common.common.resp.Result<java.lang.Object>
     */
    @PostMapping(value = "/log/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<Object> findAllUsers(HttpServletRequest request){
        String tid = ResultUtils.nextTid();
        log.info(String.format("正在请求「/log/findAll」接口，tid=[%s]", tid));
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
                dataMap.put("list", list);
                dataMap.put("pageInfo", pageInfo);
                return ResultUtils.success(tid, dataMap);
            }else{
                return ResultUtils.error(BaseResultCode.DATA_NO_RESULTS, tid);
            }
        }catch (Exception e){
            log.error("根据条件查询日志列表出现异常：", e);
            return ResultUtils.error(tid);
        }
    }

}
