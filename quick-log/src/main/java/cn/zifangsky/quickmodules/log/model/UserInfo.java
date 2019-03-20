package cn.zifangsky.quickmodules.log.model;

/**
 * 用户信息
 * @author zifangsky
 * @date 2017/12/4
 * @since 1.0.0
 */
public class UserInfo {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名（显示名称，一般设置为中文名）
     */
    private String username;

    public UserInfo(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}