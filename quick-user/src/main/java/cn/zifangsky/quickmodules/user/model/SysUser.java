package cn.zifangsky.quickmodules.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

public class SysUser {
    private Long id;

    private String username;

    private String name;

    private String password;

    private String encryptMode;

    private String phone;

    private String email;

    private Integer userType;

    private Integer institutionId;

    private Integer status;

    private Boolean isDel;

    private String loginIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    private String creater;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEncryptMode() {
        return encryptMode;
    }

    public void setEncryptMode(String encryptMode) {
        this.encryptMode = encryptMode == null ? null : encryptMode.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", encryptMode='" + encryptMode + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                ", institutionId=" + institutionId +
                ", status=" + status +
                ", isDel=" + isDel +
                ", loginIp='" + loginIp + '\'' +
                ", loginTime=" + loginTime +
                ", creater='" + creater + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
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
                Objects.equals(creater, sysUser.creater) &&
                Objects.equals(createTime, sysUser.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, name, password, encryptMode, phone, email, userType, institutionId, status, isDel, loginIp, loginTime, creater, createTime);
    }
}