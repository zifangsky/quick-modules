package cn.zifangsky.quickmodules.user.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户基本信息
 *
 * @author zifangsky
 * @date 2017/12/29
 * @since 1.0.0
 */
@Data
public class UserInfo {
    private String username;

    private String name;

    private String phone;

    private String email;

    private Integer userType;

    private Integer institutionId;

    private Integer status;

    private Boolean isDel;

    private String loginIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
