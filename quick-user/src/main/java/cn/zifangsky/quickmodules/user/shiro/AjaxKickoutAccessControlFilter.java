package cn.zifangsky.quickmodules.user.shiro;

import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.enums.AuthCodeEnums;
import com.alibaba.druid.support.json.JSONUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 踢出过滤器
 *
 * @author zifangsky
 * @date 2017/12/10
 * @since 1.0.0
 */
public class AjaxKickoutAccessControlFilter extends AccessControlFilter{

    /**
     * 是否允许继续访问 ，如果允许则返回true，否则返回false
     * @author zifangsky
     * @date 2017/12/10 14:21
     * @since 1.0.0
     * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();

        Object kickedOutFlag = session.getAttribute(Constants.KICKED_OUT_FLAG);

        if(kickedOutFlag != null){
            try {
                //删除标识
                session.removeAttribute(Constants.KICKED_OUT_FLAG);
                //退出登录
                subject.logout();
            }catch (Exception e){
                //ignore
            }

            return false;
        }

        return true;
    }

    /**
     * 处理 isAccessAllowed 方法返回false的情况，如果继续往后处理则返回true，，否则返回false
     * @author zifangsky
     * @date 2017/12/10 14:29
     * @since 1.0.0
     * @return boolean
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=UTF-8");
        Map<String,Object> result = new HashMap<>(2);
        result.put("code", AuthCodeEnums.KICKOUT.getCode());
        result.put("msg", AuthCodeEnums.KICKOUT.getMsg());

        response.getWriter().write(JSONUtils.toJSONString(result));
        return false;
    }

}
