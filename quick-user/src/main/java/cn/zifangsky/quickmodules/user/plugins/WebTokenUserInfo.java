package cn.zifangsky.quickmodules.user.plugins;

import cn.zifangsky.easylimit.session.impl.support.TokenInfo;

import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于Token模式的配置信息
 *
 * @author zifangsky
 * @date 2020/1/6
 * @since 1.0.0
 */
public class WebTokenUserInfo extends WebUserInfo {
    /**
     * token的基本参数
     */
    private TokenInfo tokenInfo;

    /**
     * 刷新Access Token的URL
     */
    private String refreshTokenUrl = "/refreshToken";

    public WebTokenUserInfo() {
        this.defaultTokenInfo();
    }

    /**
     * 开始设置需要过滤的URL
     */
    @Override
    public TokenFilterChainBuilder filter(){
        return new TokenFilterChainBuilder(this);
    }

    /**
     * 设置自定义的过滤Map
     */
    @Override
    public WebTokenUserInfo filterChainMap(FilterChain filterChain){
        this.filterChainDefinitionMap = new LinkedHashMap<>();
        LinkedHashMap<String, String[]> filterChainMap = filterChain.getFilterChainMap();

        if(filterChainMap.size() > 0){
            for (Map.Entry<String, String[]> entry : filterChainMap.entrySet()) {
                this.filterChainDefinitionMap.put(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }


    /**
     * 设置{@link TokenInfo}
     * <p>默认Access Token超时时间：10分钟</p>
     * <p>默认Refresh Token超时时间：365天</p>
     */
    public WebTokenUserInfo tokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
        return this;
    }

    /**
     * 刷新Access Token的URL
     * <p>默认：/refreshToken</p>
     */
    public WebTokenUserInfo refreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
        return this;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public String getRefreshTokenUrl() {
        return refreshTokenUrl;
    }

    /**
     * 设置默认的{@link TokenInfo}
     */
    private void defaultTokenInfo() {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessTokenTimeout(10L);
        tokenInfo.setAccessTokenTimeoutUnit(ChronoUnit.MINUTES);
        tokenInfo.setRefreshTokenTimeout(365L);
        tokenInfo.setRefreshTokenTimeoutUnit(ChronoUnit.DAYS);

        this.tokenInfo = tokenInfo;
    }

}
