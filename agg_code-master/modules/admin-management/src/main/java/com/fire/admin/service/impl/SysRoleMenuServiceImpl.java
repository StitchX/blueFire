package com.fire.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.mapper.SysRoleMenuMapper;
import com.fire.admin.service.ISysRoleMenuService;
import com.fire.dto.system.SysRoleMenu;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * @author: liuliu
 * @ClassName: SysRoleMenuServiceImpl
 * @Description: 角色菜单表 服务实现类
 * @date: 2021-05-13 19:08
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public List<Long> getMenuIdByUserId(Long userId) {
        return baseMapper.getMenuIdByUserId(userId);
    }
}
