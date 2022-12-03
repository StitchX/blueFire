package com.fire.admin.rest;


import com.fire.admin.dto.RoleDTO;
import com.fire.admin.service.ISysRoleService;
import com.fire.dto.response.BaseRestResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @classname: SysRoleController
 * @description 系统角色表 前端控制器
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Resource
    private ISysRoleService roleService;

    /**
     * @Description: 获取角色列表
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:46
     */
    @GetMapping
    @PreAuthorize("hasAuthority('sys:role:view')")
    public BaseRestResponse getRoleList(@RequestParam String roleName) {
        return new BaseRestResponse(roleService.selectRoleList(roleName));
    }

    /**
     * @Description: 保存角色以及菜单权限
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:46
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add')")
    public BaseRestResponse save(@RequestBody RoleDTO roleDto) {
        return new BaseRestResponse(roleService.saveRoleMenu(roleDto));
    }

    /**
     * @Description: 根据角色id获取菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:46
     */
    @GetMapping("/findRoleMenus/{roleId}")
    public BaseRestResponse findRoleMenus(@PathVariable("roleId") Long roleId) {
        return new BaseRestResponse(roleService.findMenuListByRoleId(roleId));
    }


    /**
     * @Description: 更新角色以及菜单权限
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:46
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:role:update')")
    public BaseRestResponse update(@RequestBody RoleDTO roleDto) {
        return new BaseRestResponse(roleService.updateRoleMenu(roleDto));
    }

    /**
     * @Description: 删除角色以及权限
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:46
     */
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public BaseRestResponse delete(@PathVariable("roleId") Long roleId) {
        return new BaseRestResponse(roleService.removeById(roleId));
    }
}

