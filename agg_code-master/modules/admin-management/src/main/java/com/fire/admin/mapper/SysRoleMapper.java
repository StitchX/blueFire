package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.dto.system.SysMenu;
import com.fire.dto.system.SysRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {


    @Insert("insert into sys_role (role_id,role_name,role_code,role_desc) values (#{roleId},#{roleName}, #{roleCode},#{roleDesc})")
    @Options(useGeneratedKeys = true, keyProperty = "roleId", keyColumn = "role_id")
    Boolean insertRole(SysRole sysRole);

    /**
     *
     * @param roleId
     * @return
     */
    @Select("select m.menu_id,m.name,m.type,m.parent_id,m.sort,m.perms from sys_menu m, sys_role_menu rm where rm.role_id = #{roleId} and m.menu_id = rm.menu_id")
    List<SysMenu> findMenuListByRoleId(Long roleId);

    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    @Select("SELECT r.* FROM sys_role r, sys_user_role ur WHERE r.role_id = ur.role_id AND r.del_flag = 0 and  ur.user_id IN (#{userId})")
    List<SysRole> listRolesByUserId(Long userId);
}
