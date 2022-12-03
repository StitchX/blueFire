package com.fire.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Classname UserDTO
 * @Description 菜单Dto
 * @Date 2019-04-23 21:26
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuDTO {

    private static final long serialVersionUID = 1L;

    private Long menuId;
    private String name;
    private String perms;
    private String path;
    private Boolean isFrame;
    private Long parentId;
    private String component;
    private String icon;
    private Integer sort;
    private Integer type;
    private String delFlag;

}
