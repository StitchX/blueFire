package com.fire.dto.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;


/**
 * @author: liuliu
 * @ClassName: SysUser
 * @Description: 用户表
 * @date: 2021-05-14 15:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;


    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 监管局、监管所id
     */
    private Long supervisionId;

    /**
     * 岗位ID
     */
    private Long jobId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 0-正常，1-锁定
     */
    private String lockFlag;

    /**
     * 0-正常，1-删除
     */
    private String delFlag;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    private List<SysRole> roleList;
    /**
     * 非数据库字段
     * 监管局名称
     */
    @TableField(exist = false)
    private String supervisionName;

    @TableField(exist = false)
    private String key;
    /**
     * 当前用户对应的监管所或者监管局对应的监管所
     */
    @TableField(exist = false)
    private Set<Long> supervisionIds;


}
