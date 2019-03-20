package cn.zifangsky.quickmodules.user.utils;

import cn.zifangsky.quickmodules.user.common.Constants;
import cn.zifangsky.quickmodules.user.model.SysUser;
import cn.zifangsky.quickmodules.user.shiro.CustomRealm;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import java.io.Serializable;
import java.util.Collection;

/**
 * Shiro相关公共方法
 *
 * @author zifangsky
 * @date 2017/12/7
 * @since 1.0.0
 */
public class ShiroUtils {

    /**
     * 清空缓存
     * @author zifangsky
     * @date 2017/12/7 16:17
     * @since 1.0.0
     */
    public static synchronized void clearAuthorizationInfo(){
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        CustomRealm customRealm = (CustomRealm) (securityManager.getRealms().iterator().next());
        //清空
        customRealm.clearAuthorizationInfo();
    }

    /**
     * 删除某个用户的旧session
     * @author zifangsky
     * @date 2017/12/7 16:17
     * @since 1.0.0
     * @param username 用户名
     */
    public static synchronized void deleteOldSession(String username){
        if(StringUtils.isNoneBlank(username)){
            try {
                Subject subject = SecurityUtils.getSubject();
                //当前会话
                Session currentSession = subject.getSession();
                DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
                DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
                SessionDAO sessionDAO = sessionManager.getSessionDAO();

                //获取当前所有活跃会话
                Collection<Session> sessions = sessionDAO.getActiveSessions();
                SysUser user = null;

                if(sessions != null){
                    //遍历所有会话
                    for(Session session : sessions){
                        Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                        if (attribute == null) {
                            continue;
                        }

                        user = (SysUser) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
                        //如果是同一个用户，而且又不是同一个会话，则删除该用户的旧session
                        if(username.equals(user.getUsername()) && !checkSessionId(currentSession.getId(), session.getId())) {
                            //表示该session将被踢出
                            session.setAttribute(Constants.KICKED_OUT_FLAG, "1");
                        }
                    }
                }
            }catch (Exception e){
                //ignore
            }
        }
    }

    /**
     * 刷新session的最新访问时间
     * @author zifangsky
     * @date 2017/1/8 16:19
     * @since 1.0.0
     */
    public static void updateSessionLastAccessTime() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            //刷新session的最新访问时间
            session.touch();
        }catch (Exception e){
            //ignore
        }
    }

    /**
     * 刷新session的最新访问时间
     * @author zifangsky
     * @date 2017/1/8 16:19
     * @since 1.0.0
     */
    public static void updateSessionLastAccessTime(String username) {
        if(StringUtils.isNoneBlank(username)){
            try {
                DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
                DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
                SessionDAO sessionDAO = sessionManager.getSessionDAO();

                //获取当前已经登录的用户列表
                Collection<Session> sessions = sessionDAO.getActiveSessions();
                SysUser user = null;

                if(sessions != null){
                    for(Session session : sessions){
                        Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                        if (attribute == null) {
                            continue;
                        }

                        user = (SysUser) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
                        //刷新session的最新访问时间
                        if(username.equals(user.getUsername())) {
                            session.touch();
                        }
                    }
                }
            }catch (Exception e){
                //ignore
            }
        }
    }

    /**
     * 判断两个sessionId是否属于同一个sessionId
     */
    private static boolean checkSessionId(Serializable sessionId1, Serializable sessionId2){
        if(sessionId1 == null && sessionId2 == null){
            return true;
        }

        if(sessionId1 != null && sessionId2 != null && String.valueOf(sessionId1).equals(String.valueOf(sessionId2))){
            return true;
        }

        return false;
    }

}
