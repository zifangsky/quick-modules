package cn.zifangsky.quickmodules.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

@Data
public class SysRole {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态（0:正常；1：删除）
     */
    private Integer status;

    /**
     * 公司id
     */
    private Integer institutionId;

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
        SysRole sysRole = (SysRole) o;
        return Objects.equals(id, sysRole.id) &&
                Objects.equals(name, sysRole.name) &&
                Objects.equals(description, sysRole.description) &&
                Objects.equals(status, sysRole.status) &&
                Objects.equals(institutionId, sysRole.institutionId) &&
                Objects.equals(creator, sysRole.creator) &&
                Objects.equals(createTime, sysRole.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, institutionId, creator, createTime);
    }
}