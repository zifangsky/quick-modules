package cn.zifangsky.quickmodules.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

@Data
public class SysFunction {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 自身唯一标识ID
     */
    private String myselfId;

    /**
     * 父级资源项ID ,取myselfid值
     */
    private String parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 资源类型： 1.菜单   2.按钮
     */
    private Integer type;

    /**
     * 层级：1.一级导航菜单  2.二级导航菜单  3.功能按钮
     */
    private Integer level;

    /**
     * 权限路径
     */
    private String pathUrl;

    /**
     * 图标路径
     */
    private String iconUrl;

    /**
     * 排序
     */
    private Integer sequenceNum;

    /**
     * 状态（0:正常；1：删除）
     */
    private Integer state;

    /**
     * 资源项描述
     */
    private String description;

    /**
     * 公司id
     */
    private String institutionId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
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
                Objects.equals(creator, that.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, myselfId, parentId, name, type, level, pathUrl, iconUrl, sequenceNum, state, description, institutionId, createTime, creator);
    }
}