package cn.zifangsky.quickmodules.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

public class SysRole {
    private Long id;

    private String name;

    private String description;

    private Integer status;

    private Integer institutionId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
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
        return "SysRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", institutionId=" + institutionId +
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

        SysRole sysRole = (SysRole) o;
        return Objects.equals(id, sysRole.id) &&
                Objects.equals(name, sysRole.name) &&
                Objects.equals(description, sysRole.description) &&
                Objects.equals(status, sysRole.status) &&
                Objects.equals(institutionId, sysRole.institutionId) &&
                Objects.equals(creater, sysRole.creater) &&
                Objects.equals(createTime, sysRole.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, institutionId, creater, createTime);
    }
}