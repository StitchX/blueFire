package com.fire.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.dto.RoleDTO;
import com.fire.admin.mapper.SysRoleMapper;
import com.fire.admin.service.ISysRoleDeptService;
import com.fire.admin.service.ISysRoleMenuService;
import com.fire.admin.service.ISysRoleService;
import com.fire.dto.system.SysMenu;
import com.fire.dto.system.SysRole;
import com.fire.dto.system.SysRoleDept;
import com.fire.dto.system.SysRoleMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @classname: SysRoleServiceImpl
 * @description 系统角色表 服务实现类
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private ISysRoleMenuService roleMenuService;

    @Resource
    private ISysRoleDeptService roleDeptService;



    /**
     * @Description: 保存角色和菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:46
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRoleMenu(RoleDTO roleDto) {
        log.info("roleDto:{}", roleDto.toString());
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(roleDto, sysRole);
        log.info("insert sysRole :[{}]", sysRole.toString());
        baseMapper.insertRole(sysRole);
        Long roleId = sysRole.getRoleId();
        //维护角色菜单
        List<SysRoleMenu> roleMenus = roleDto.getRoleMenus();
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            List<SysRoleMenu> rms = roleMenus.stream().map(sysRoleMenu -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(sysRoleMenu.getMenuId());
                return roleMenu;
            }).collect(Collectors.toList());
            roleMenuService.saveBatch(rms);
        }
        return true;
    }

    /**
     * @Description: 更新角色和菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleMenu(RoleDTO roleDto) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(roleDto, sysRole);

        List<SysRoleMenu> roleMenus = roleDto.getRoleMenus();
        roleMenuService.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, sysRole.getRoleId()));
        roleDeptService.remove(Wrappers.<SysRoleDept>query().lambda().eq(SysRoleDept::getRoleId, sysRole.getRoleId()));
        //  遍历集合，并对  SysRoleMenu 的主键赋值
        if (CollectionUtil.isNotEmpty(roleMenus)) {
            roleMenuService.saveBatch(roleMenus);
        }
        // 根据数据权限范围查询部门ids
        baseMapper.updateById(sysRole);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        roleMenuService.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, id));
        roleDeptService.remove(Wrappers.<SysRoleDept>query().lambda().eq(SysRoleDept::getRoleId, id));
        return super.removeById(id);
    }

    /**
     * @Description: 获取角色列表
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    @Override
    public List<SysRole> selectRoleList(String roleName) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotEmpty(roleName)) {
            sysRoleLambdaQueryWrapper.like(SysRole::getRoleName, roleName);
        }
        List<SysRole> sysRoles = baseMapper.selectList(sysRoleLambdaQueryWrapper);
        return sysRoles.stream().peek(sysRole -> sysRole.setRoleDepts(roleDeptService.getRoleDeptIds(sysRole.getRoleId()).stream().map(SysRoleDept::getDeptId).collect(Collectors.toList()))).collect(Collectors.toList());
    }


    /**
     * @Description: 根据角色id获取菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    @Override
    public List<SysMenu> findMenuListByRoleId(Long roleId) {
        return baseMapper.findMenuListByRoleId(roleId);
    }

    /**
     * @Description: 通过用户ID，查询角色信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:47
     */
    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return baseMapper.listRolesByUserId(userId);
    }

}
