package cn.zifangsky.quickmodules.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

@Data
public class SysUser{
    /**
     * 主键id
     */
    private Long id;

    /**
     * 登录名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码加密方式
     */
    private String encryptMode;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 0：后台用户；1：前台用户
     */
    private Integer userType;

    /**
     * 机构id
     */
    private Integer institutionId;

    /**
     * 用户状态（0:可用；1:锁定；2:未开通）
     */
    private Integer status;

    /**
     * 删除状态：0正常；1删除
     */
    private Boolean isDel;

    private String loginIp;

    /**
     * 上次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysUser sysUser = (SysUser) o;
        return Objects.equals(id, sysUser.id) &&
                Objects.equals(username, sysUser.username) &&
                Objects.equals(name, sysUser.name) &&
                Objects.equals(password, sysUser.password) &&
                Objects.equals(encryptMode, sysUser.encryptMode) &&
                Objects.equals(phone, sysUser.phone) &&
                Objects.equals(email, sysUser.email) &&
                Objects.equals(userType, sysUser.userType) &&
                Objects.equals(institutionId, sysUser.institutionId) &&
                Objects.equals(status, sysUser.status) &&
                Objects.equals(isDel, sysUser.isDel) &&
                Objects.equals(loginIp, sysUser.loginIp) &&
                Objects.equals(loginTime, sysUser.loginTime) &&
                Objects.equals(creator, sysUser.creator) &&
                Objects.equals(createTime, sysUser.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, name, password, encryptMode, phone, email, userType, institutionId, status, isDel, loginIp, loginTime, creator, createTime);
    }
}