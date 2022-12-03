package com.fire.admin.rest;

import com.fire.admin.dto.MenuDTO;
import com.fire.admin.service.ISysMenuService;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.util.PreUtil;
import com.fire.admin.util.SecurityUtil;
import com.fire.dto.response.BaseRestResponse;
import com.fire.dto.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @classname: SysMenuController
 * @description 菜单权限表 前端控制器
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService menuService;



    /**
     * @Description: 添加菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:41
     */
    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping
    public BaseRestResponse save(@RequestBody SysMenu menu) {
        return new BaseRestResponse(menuService.save(menu));
    }

    /**
     * @Description: 获取菜单树
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:41
     */
    @GetMapping
    public BaseRestResponse getMenuTree() {
        PreSecurityUser securityUser = SecurityUtil.getUser();
        return new BaseRestResponse(menuService.selectMenuTree(securityUser.getUserId()));
    }


    /**
     * @Description: 获取所有菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:41
     */
    @GetMapping("/getMenus")
    public BaseRestResponse getMenus() {
        return new BaseRestResponse(menuService.selectMenuTree(0L));
    }

    /**
     * @Description: 修改菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:41
     */
    @PreAuthorize("hasAuthority('sys:menu:update')")
    @PutMapping
    public BaseRestResponse updateMenu(@RequestBody MenuDTO menuDto) {
        return new BaseRestResponse(menuService.updateMenuById(menuDto));
    }

    /**
     * @Description: 根据id删除菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:42
     */
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @DeleteMapping("/{id}")
    public BaseRestResponse deleteMenu(@PathVariable("id") Long id) {
        return new BaseRestResponse(menuService.removeMenuById(id));
    }

    /**
     * @Description: 获取路由
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:42
     */
    @GetMapping("/getRouters")
    public BaseRestResponse getRouters() {
        PreSecurityUser securityUser = SecurityUtil.getUser();
        return new BaseRestResponse(PreUtil.buildMenus(menuService.selectMenuTree(securityUser.getUserId())));
    }

}

