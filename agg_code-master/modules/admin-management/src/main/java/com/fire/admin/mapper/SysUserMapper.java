package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.dto.UserDTO;
import com.fire.dto.system.SysUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: liuliu
 * @ClassName: SysUserRoleMapper
 * @Description:  用户角色表 Mapper 接口
 * @date: 2021-05-14 15:40
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Insert("insert into sys_user (user_id,username,password,supervision_id,job_id,phone,email,avatar,lock_flag) values (#{userId},#{username},#{password},#{supervisionId},#{jobId},#{phone},#{email},#{avatar},#{lockFlag})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    boolean insertUser(SysUser sysUser);

    /**
     * 分页查询用户信息（含角色）
     *
     * @param page    分页
     * @param userDTO 查询参数
     * @return list
     */
    IPage<SysUser> getUserVosPage(Page page, @Param("query") UserDTO userDTO);


    @Select("select username from sys_user where user_id=#{userId}")
    String getCustomerUserName(Long userId);

    @Select("select * from sys_user where del_flag = '0'")
    List<SysUser> queryUsersAuthority();

}
