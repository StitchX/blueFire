package com.fire.dto.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @author: liuliu
 * @ClassName: SysDept
 * @Description: 部门管理
 * @date: 2021-05-14 15:13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept")
public class SysDept extends Model<SysDept> {

    private static final long serialVersionUID = 1L;

    /**
     * 部门主键ID
     */
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    /**
     * 部门名称
     */
    private String name;


    /**
     * 上级部门
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 是否删除  -1：已删除  0：正常
     */
    private String delFlag;

    /**
     * 非数据库字段
     * 上级部门
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 非数据库字段
     * 等级
     */
    @TableField(exist = false)
    private Integer level;

    /**
     * 非数据库字段
     * 子部门
     */
    @TableField(exist = false)
    private List<SysDept> children;


}
