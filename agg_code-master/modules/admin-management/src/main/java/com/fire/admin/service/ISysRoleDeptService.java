package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.dto.system.SysRoleDept;

import java.util.List;


/**
 * @author: liuliu
 * @ClassName: ISysRoleDeptService
 * @Description: 角色与部门对应关系 服务类
 * @date: 2021-05-13 19:08
 */
public interface ISysRoleDeptService extends IService<SysRoleDept> {


    /**
     * @Description: 根据角色id查询部门ids
     * @Param: [roleId]
     * @return: java.util.List<com.fire.dto.system.SysRoleDept>
     * @Author: liuliu
     * @Date: 2021/5/13 18:58
     */
    List<SysRoleDept> getRoleDeptIds(Long roleId);
}
