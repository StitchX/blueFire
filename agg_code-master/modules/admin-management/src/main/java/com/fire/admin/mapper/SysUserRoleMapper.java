package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.dto.system.SysUserRole;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author: liuliu
 * @ClassName: SysUserRoleMapper
 * @Description: 用户角色表 Mapper 接口
 * @date: 2021-05-14 15:40
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Override
    int insert(SysUserRole entity);

    @Select("SELECT r.role_name,ur.role_id \n" + "FROM (sys_role r LEFT JOIN sys_user_role ur ON r.role_id = ur.role_id ) \n" + "LEFT JOIN sys_user u ON u.user_id = ur.user_id WHERE u.user_id = #{userId}")
    List<SysUserRole> selectUserRoleListByUserId(Long userId);
}
