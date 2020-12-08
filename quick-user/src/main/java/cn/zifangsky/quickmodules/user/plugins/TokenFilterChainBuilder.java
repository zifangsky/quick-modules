package cn.zifangsky.quickmodules.user.plugins;

import cn.zifangsky.easylimit.filter.impl.support.DefaultFilterEnums;

import java.util.LinkedHashMap;

/**
 * 基于Token模式的{@link FilterChainBuilder}
 *
 * @author zifangsky
 * @date 2020/1/6
 * @since 1.0.0
 */
public class TokenFilterChainBuilder extends FilterChainBuilder {
    private WebTokenUserInfo parent;

    public TokenFilterChainBuilder(WebTokenUserInfo parent) {
        super(parent);
        this.parent = parent;
    }

    /**
     * 设置自定义的过滤Map
     */
    @Override
    public TokenFilterChainBuilder filterChainMap(LinkedHashMap<String, String[]> filterChainDefinitionMap) {
        if(filterChainDefinitionMap != null){
            this.filterChainDefinitionMap = filterChainDefinitionMap;
        }

        return this;
    }

    /**
     * 生成{@link WebUserInfo}
     */
    @Override
    public WebTokenUserInfo build(){
        //如果没有自定义的PluginManager，则new一个
        if(this.parent.getPluginManager() == null){
            this.parent.pluginManager(new PluginManager());
        }

        //添加几个默认的过滤信息
        LinkedHashMap<String, String[]> result = new LinkedHashMap<>();
        this.addDefaultFilterChain(result);
        result.put(this.parent.getRefreshTokenUrl(), new String[]{DefaultFilterEnums.ANONYMOUS.getFilterName()});

        result.putAll(filterChainDefinitionMap);

        return this.parent.filterChainMap(new FilterChain(result));
    }

}
