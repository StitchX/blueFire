package com.fire.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.constant.MenuConstant;
import com.fire.admin.dto.MenuDTO;
import com.fire.admin.mapper.SysMenuMapper;
import com.fire.admin.service.ISysMenuService;
import com.fire.admin.service.ISysRoleMenuService;
import com.fire.admin.util.PreUtil;
import com.fire.common.exception.BaseException;
import com.fire.dto.response.BaseRestResponse;
import com.fire.dto.system.SysMenu;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @classname: SysMenuServiceImpl
 * @description 菜单权限表 服务实现类
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Override
    public boolean save(SysMenu sysMenu) {
        // 菜单校验
        verifyForm(sysMenu);
        return super.save(sysMenu);
    }

    /**
     * @Description: 更新菜单信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:42
     */
    @Override
    public boolean updateMenuById(MenuDTO entity) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(entity, sysMenu);
        // 菜单校验
        verifyForm(sysMenu);
        return this.updateById(sysMenu);
    }

    /**
     * @Description: 根据用户id查找菜单树
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:42
     */
    @Override
    public List<SysMenu> selectMenuTree(Long uid) {
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = Wrappers.<SysMenu>query().lambda();
        sysMenuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getName, SysMenu::getPerms, SysMenu::getPath, SysMenu::getParentId, SysMenu::getComponent, SysMenu::getIsFrame, SysMenu::getIcon, SysMenu::getSort, SysMenu::getType, SysMenu::getDelFlag);
        // 所有人有权限看到 只是没有权限操作而已 暂定这样
        if (!uid.equals(0L)) {
            List<Long> menuIdList = roleMenuService.getMenuIdByUserId(uid);
            sysMenuLambdaQueryWrapper.in(SysMenu::getMenuId, menuIdList);
        }
        List<SysMenu> sysMenus = new ArrayList<>();
        List<SysMenu> menus = baseMapper.selectList(sysMenuLambdaQueryWrapper);
        menus.forEach(menu -> {
            if (ObjectUtil.isEmpty(menu.getParentId()) || menu.getParentId().equals(0L)) {
                menu.setLevel(0);
                if (PreUtil.exists(sysMenus, menu)) {
                    sysMenus.add(menu);
                }
            }
        });
        sysMenus.sort((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
        PreUtil.findChildren(sysMenus, menus, 0);
        return sysMenus;
    }

    /**
     * @Description: 根据父id查询菜单
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:43
     */
    @Override
    public SysMenu getMenuById(Long parentId) {
        return baseMapper.selectOne(Wrappers.<SysMenu>lambdaQuery().select(SysMenu::getType).eq(SysMenu::getMenuId, parentId));
    }

    /**
     * @Description: 根据用户id查询权限
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:43
     */
    @Override
    public List<String> findPermsByUserId(Long userId) {
        return baseMapper.findPermsByUserId(userId);
    }

    @Override
    public BaseRestResponse removeMenuById(Serializable id) {
        List<Long> idList = this.list(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id)).stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(idList)) {
            throw new BaseException("菜单含有下级不能删除");
        }
        return new BaseRestResponse(this.removeById(id));
    }

    /**
     * @Description: 验证菜单参数是否正确
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:44
     */
    private void verifyForm(SysMenu menu) {
        //上级菜单类型
        int parentType = MenuConstant.MenuType.CATALOG.getValue();
        if (!menu.getParentId().equals(0L)) {
            SysMenu parentMenu = getMenuById(menu.getParentId());
            parentType = parentMenu.getType();
        }
        //目录、菜单
        if (menu.getType() == MenuConstant.MenuType.CATALOG.getValue() || menu.getType() == MenuConstant.MenuType.MENU.getValue()) {
            if (parentType != MenuConstant.MenuType.CATALOG.getValue()) {
                throw new BaseException("上级菜单只能为目录类型");
            }
            return;
        }
        //按钮
        if (menu.getType() == MenuConstant.MenuType.BUTTON.getValue()) {
            if (parentType != MenuConstant.MenuType.MENU.getValue()) {
                throw new BaseException("上级菜单只能为菜单类型");
            }
        }
    }
}
