package cn.zifangsky.quickmodules.log.model;

import lombok.Data;

/**
 * 用户信息
 * @author zifangsky
 * @date 2017/12/4
 * @since 1.0.0
 */
@Data
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

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}