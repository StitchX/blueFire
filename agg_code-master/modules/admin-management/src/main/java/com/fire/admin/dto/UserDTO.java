package com.fire.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @Classname UserDTO
 * @Description 用户Dto
 * @Date 2019-04-23 21:26
 * @Version 1.0
 */
@Data
public class UserDTO implements Serializable {

    private Long userId;
    private String username;
    private String password;
    private Long supervisionId;
    private String phone;
    private String email;
    private String avatar;
    private String lockFlag;
    private String delFlag;
    private List<Long> roleList;
    private Set<Long> supervisionIdList;

    /**
     * 1 :管理员  2：客户  3： 中间人
     */
    private Integer type;

    /**
     * 新密码
     */
    private String newPassword;
}
