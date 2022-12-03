package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.dto.system.SysUserRole;

import java.util.List;


/**
 * @author: liuliu
 * @ClassName: ISysUserRoleService
 * @Description: 用户角色表 服务类
 * @date: 2021-05-13 19:08
 */
public interface ISysUserRoleService extends IService<SysUserRole> {


    /**
     * @Description: 根据用户id查询用户角色关系
     * @Param: [userId]
     * @return: java.util.List<com.fire.dto.system.SysUserRole>
     * @Author: liuliu
     * @Date: 2021/5/13 18:56
     */
    List<SysUserRole> selectUserRoleListByUserId(Long userId);
}
