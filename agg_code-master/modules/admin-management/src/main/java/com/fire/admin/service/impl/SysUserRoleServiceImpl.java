package com.fire.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.mapper.SysUserRoleMapper;
import com.fire.admin.service.ISysUserRoleService;
import com.fire.dto.system.SysUserRole;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * @author: liuliu
 * @ClassName: SysUserRoleServiceImpl
 * @Description: 用户角色表 服务实现类
 * @date: 2021-05-13 19:08
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {


    @Override
    public boolean save(SysUserRole entity) {
        return super.save(entity);
    }


    @Override
    public List<SysUserRole> selectUserRoleListByUserId(Long userId) {
        return baseMapper.selectUserRoleListByUserId(userId);
    }
}
