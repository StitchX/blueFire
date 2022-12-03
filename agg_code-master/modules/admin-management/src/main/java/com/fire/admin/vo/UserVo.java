package com.fire.admin.vo;

import lombok.Data;

/**
 * @author: liuliu
 * @ClassName: UserVo
 * @Description:
 * @date: 2021-05-13 16:11
 */
@Data
public class UserVo {

    private Long userId;

    private String userName;

    private String phone;

    private String email;

    private String avatar;

    private String deptId;

    private String createTime;

    private String updateTime;

    /**
     * 0-正常，1-锁定
     */
    private String lockFlag;

    /**
     * 0-正常，1-删除
     */
    private String delFlag;

    private String deptName;


}
