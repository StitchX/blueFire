package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.dto.system.SysRoleMenu;

import java.util.List;


/**
 * @author: liuliu
 * @ClassName: ISysRoleMenuService
 * @Description: 角色菜单表 服务类
 * @date: 2021-05-13 19:08
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * @Description: 根据用户id获取菜单的id列表
     * @Param: [userId]
     * @return: java.util.List<java.lang.Integer>
     * @Author: liuliu
     * @Date: 2021/5/13 18:58
     */
    List<Long> getMenuIdByUserId(Long userId);

}
