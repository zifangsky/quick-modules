package cn.zifangsky.quickmodules.log.plugins;

import cn.zifangsky.quickmodules.log.model.UserInfo;

/**
 * 对外暴露出去的方法
 *
 * @author zifangsky
 * @date 2017/12/4
 * @since 1.0.0
 */
public abstract class AbstractLogManager {

    /**
     * 获取用户基本信息
     * @author zifangsky
     * @date 2017/12/4 16:46
     * @since 1.0.0
     * @return cn.zifangsky.quickmodules.log.model.UserInfo
     */
    public abstract UserInfo getUserInfo();





}
