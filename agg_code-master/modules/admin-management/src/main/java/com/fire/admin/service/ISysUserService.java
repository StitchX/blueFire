package com.fire.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.UserDTO;
import com.fire.dto.system.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


/**
 * @classname: ISysUserService
 * @description 用户表 服务类
 * @author: liu liu
 * @create: 2020-08-13 14:51
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * @Description: 分页查询用户信息（含有角色信息）
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    IPage<SysUser> getUsersWithRolePage(Page page, UserDTO userDTO);

    /**
     * @Description: 保存用户以及角色部门等信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    boolean insertUser(UserDTO userDto);

    /**
     * @Description: 更新用户以及角色部门等信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    boolean updateUser(UserDTO userDto);

    /**
     * @Description: 删除用户信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    boolean removeUser(Long userId);

    /**
     * @Description: 重置密码
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    boolean restPass(Long userId);

    /**
     * @Description: 通过用户名查找用户个人信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    SysUser findByUserInfoName(String username);

    /**
     * @Description: 根据用户id查询权限
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    Set<String> findPermsByUserId(Long userId);

    /**
     * @Description: 通过用户id查询角色集合
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    Set<String> findRoleIdByUserId(Long userId);

    /**
     * @Description: 账户密码登录
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    String login(String username, String password, HttpServletRequest request);

    /**
     * @Description: 注册用户
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    boolean register(UserDTO userDTO);

    /**
     * @Description: 修改用户信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    boolean updateUserInfo(SysUser sysUser);

    /**
     * @Description: 通过用户去查找用户(id / 用户名 / 手机号)
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    SysUser findSecurityUserByUser(SysUser sysUser);

    /**
     *@description:  通过用户所在的监管所、监管员获取旗下的监管所或者监管员
     * @param supervisionBureauId
     *@return: java.util.Set<java.lang.Long>
     *@author: liuliu
     *@date: 2022-01-28 14:40
    */
    Set<Long> findSupervisionBureaus(Long supervisionBureauId);


}
