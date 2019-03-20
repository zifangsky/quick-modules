package cn.zifangsky.quickmodules.log.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SysLog {
    private Long id;

    private String content;

    private Integer type;

    private String module;

    private Long userId;

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    private Long takeTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module == null ? null : module.trim();
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
        this.username = username == null ? null : username.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Long takeTime) {
        this.takeTime = takeTime;
    }

    @Override
    public String toString() {
        return "SysLog{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", module='" + module + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", startTime=" + startTime +
                ", takeTime=" + takeTime +
                '}';
    }
}