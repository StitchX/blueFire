package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.RoleDTO;
import com.fire.dto.system.SysMenu;
import com.fire.dto.system.SysRole;

import java.io.Serializable;
import java.util.List;


/**
 * @classname: ISysRoleService
 * @description 系统角色表 服务类
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * @Description: 保存角色和菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:46
     */
    boolean saveRoleMenu(RoleDTO roleDto);

    /**
     * @Description: 更新角色和菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    boolean updateRoleMenu(RoleDTO roleDto);

    /**
     * @Description: 根据主键删除角色
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * @Description: 获取角色列表
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    List<SysRole> selectRoleList(String roleName);

    /**
     * @Description: 根据角色id获取菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    List<SysMenu> findMenuListByRoleId(Long roleId);

    /**
     * @Description: 通过用户ID，查询角色信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    List<SysRole> findRolesByUserId(Long userId);
}
