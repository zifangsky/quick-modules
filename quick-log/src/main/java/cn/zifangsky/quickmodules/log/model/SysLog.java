package cn.zifangsky.quickmodules.log.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
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