package cn.zifangsky.quickmodules.user.plugins;

import cn.zifangsky.easylimit.filter.impl.support.DefaultFilterEnums;

import java.util.LinkedHashMap;

/**
 * 用于组装过滤URL
 *
 * @author zifangsky
 * @date 2018/11/5
 * @since 1.0.0
 */
public class FilterChainBuilder {
    private WebUserInfo parent;
    /**
     * 自定义的过滤Map
     */
    protected LinkedHashMap<String, String[]> filterChainDefinitionMap;

    public FilterChainBuilder(WebUserInfo parent) {
        this.parent = parent;
        this.filterChainDefinitionMap = new LinkedHashMap<>();
    }

    /**
     * 设置自定义的过滤Map
     */
    public FilterChainBuilder filterChainMap(LinkedHashMap<String, String[]> filterChainDefinitionMap) {
        if(filterChainDefinitionMap != null){
            this.filterChainDefinitionMap = filterChainDefinitionMap;
        }

        return this;
    }

    protected void addDefaultFilterChain(LinkedHashMap<String, String[]> result){
        result.put(parent.getLoginUrl(), new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        result.put(parent.getLoginCheckUrl(), new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        result.put(parent.getAuthImageUrl(), new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        result.put(parent.getLoginPhoneVerifyCodeUrl(), new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        result.put(parent.getUnauthorizedUrl(), new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        result.put(parent.getUnauthorizedUrl(), new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
    }

    /**
     * 生成{@link WebUserInfo}
     */
    public WebUserInfo build(){
        //如果没有自定义的PluginManager，则new一个
        if(parent.getPluginManager() == null){
            parent.pluginManager(new PluginManager());
        }

        //添加几个默认的过滤信息
        LinkedHashMap<String, String[]> result = new LinkedHashMap<>();
        this.addDefaultFilterChain(result);

        if (parent instanceof WebTokenUserInfo){
            result.put(((WebTokenUserInfo) parent).getRefreshTokenUrl(), new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});
        }

        result.putAll(filterChainDefinitionMap);

        return parent.filterChainMap(new FilterChain(result));
    }
}
