package cn.zifangsky.quickmodules.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

public class SysFunction {
    private Integer id;

    private String myselfId;

    private String parentId;

    private String name;

    private Integer type;

    private Integer level;

    private String pathUrl;

    private String iconUrl;

    private Integer sequenceNum;

    private Integer state;

    private String description;

    private String institutionId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String creater;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMyselfId() {
        return myselfId;
    }

    public void setMyselfId(String myselfId) {
        this.myselfId = myselfId == null ? null : myselfId.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl == null ? null : pathUrl.trim();
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl == null ? null : iconUrl.trim();
    }

    public Integer getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(Integer sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId == null ? null : institutionId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    @Override
    public String toString() {
        return "SysFunction{" +
                "id=" + id +
                ", myselfId='" + myselfId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", level=" + level +
                ", pathUrl='" + pathUrl + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", sequenceNum=" + sequenceNum +
                ", state=" + state +
                ", description='" + description + '\'' +
                ", institutionId='" + institutionId + '\'' +
                ", createTime=" + createTime +
                ", creater='" + creater + '\'' +
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

        SysFunction that = (SysFunction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(myselfId, that.myselfId) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(level, that.level) &&
                Objects.equals(pathUrl, that.pathUrl) &&
                Objects.equals(iconUrl, that.iconUrl) &&
                Objects.equals(sequenceNum, that.sequenceNum) &&
                Objects.equals(state, that.state) &&
                Objects.equals(description, that.description) &&
                Objects.equals(institutionId, that.institutionId) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(creater, that.creater);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, myselfId, parentId, name, type, level, pathUrl, iconUrl, sequenceNum, state, description, institutionId, createTime, creater);
    }
}