package com.fire.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Classname UserDTO
 * @Description 部门Dto
 * @Date 2019-04-23 21:26
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeptDTO {

    private static final long serialVersionUID = 1L;

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


}
