package com.blue.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.dto.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author: Cohen
 * @ClassName: SysUserRoleMapper
 * @Description:  用户角色表 Mapper 接口
 * @date: 2022-02-18 15:40
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
