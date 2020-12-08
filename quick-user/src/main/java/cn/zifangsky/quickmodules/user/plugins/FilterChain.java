package cn.zifangsky.quickmodules.user.plugins;

import java.util.LinkedHashMap;

/**
 * FilterChain
 *
 * @author zifangsky
 * @date 2018/11/5
 * @since 1.0.0
 */
public class FilterChain {
    /**
     * 自定义的过滤Map
     */
    private LinkedHashMap<String, String[]> filterChainMap;

    public FilterChain(LinkedHashMap<String, String[]> filterChainMap) {
        this.filterChainMap = filterChainMap;
    }

    public LinkedHashMap<String, String[]> getFilterChainMap() {
        return filterChainMap;
    }
}
