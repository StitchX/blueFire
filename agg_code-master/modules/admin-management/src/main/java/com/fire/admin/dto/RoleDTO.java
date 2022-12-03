package com.fire.admin.dto;

import com.fire.dto.system.SysRoleMenu;
import lombok.Data;

import java.util.List;

/**
 * @Classname UserDTO
 * @Description 角色Dto
 * @Date 2019-04-23 21:26
 * @Version 1.0
 */
@Data
public class RoleDTO {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private String roleName;
    private String roleCode;
    private String roleDesc;
    private String delFlag;
    private int dsType;
    List<SysRoleMenu> roleMenus;
    List<Long> roleDepts;


}
