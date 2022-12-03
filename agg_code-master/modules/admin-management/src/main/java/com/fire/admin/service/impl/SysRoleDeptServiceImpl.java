package com.fire.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.mapper.SysRoleDeptMapper;
import com.fire.admin.service.ISysRoleDeptService;
import com.fire.dto.system.SysRoleDept;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * @author: liuliu
 * @ClassName: SysDictServiceImpl
 * @Description: 角色与部门对应关系 服务实现类
 * @date: 2021-05-13 19:08
 */
@Service
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements ISysRoleDeptService {


    @Override
    public List<SysRoleDept> getRoleDeptIds(Long roleId) {
        return baseMapper.selectList(Wrappers.<SysRoleDept>lambdaQuery().select(SysRoleDept::getDeptId).eq(SysRoleDept::getRoleId, roleId));
    }
}
