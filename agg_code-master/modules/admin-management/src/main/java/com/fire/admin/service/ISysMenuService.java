package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.MenuDTO;
import com.fire.dto.response.BaseRestResponse;
import com.fire.dto.system.SysMenu;

import java.io.Serializable;
import java.util.List;

/**
 * @classname: ISysMenuService
 * @description 菜单权限表 服务类
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * @Description: 更新菜单信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:42
     */
    boolean updateMenuById(MenuDTO entity);

    /**
     * @Description: 删除菜单信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:42
     */
    BaseRestResponse removeMenuById(Serializable id);

    /**
     * @Description: 根据用户id查找菜单树
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:42
     */
    List<SysMenu> selectMenuTree(Long uid);

    /**
     * @Description: 根据父id查询菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:43
     */
    SysMenu getMenuById(Long parentId);

    /**
     * @Description: 根据用户id查询权限
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:43
     */
    List<String> findPermsByUserId(Long userId);
}
