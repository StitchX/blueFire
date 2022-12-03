package com.fire.admin.rest;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.dto.UserDTO;
import com.fire.admin.service.ISysUserService;
import com.fire.admin.util.PreUtil;
import com.fire.admin.util.SecurityUtil;
import com.fire.common.exception.BaseException;
import com.fire.dto.response.BaseRestResponse;
import com.fire.dto.system.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @classname: SysUserController
 * @description 用户表 前端控制器
 * @author: liu liu
 * @create: 2020-08-13 14:51
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户相关的接口")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    /**
     * @Description: 保存用户包括角色和部门
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:49
     */
    @ApiOperation("用户新增")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add')")
    public BaseRestResponse insert(@RequestBody UserDTO userDto) {
        return new BaseRestResponse(userService.insertUser(userDto));
    }


    /**
     * @Description: 获取用户列表集合
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:49
     */
    @ApiOperation("用户列表")
    @GetMapping
    @PreAuthorize("hasAuthority('sys:user:view')")
    public BaseRestResponse getList(Page page, UserDTO userDTO) {
        return new BaseRestResponse(userService.getUsersWithRolePage(page, userDTO));
    }

    /**
     * @Description: 更新用户包括角色和部门
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:49
     */
    @ApiOperation("用户更新")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:update')")
    public BaseRestResponse update(@RequestBody UserDTO userDto) {
        return new BaseRestResponse(userService.updateUser(userDto));
    }

    /**
     * @Description: 删除用户包括角色和部门
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:50
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public BaseRestResponse delete(@PathVariable("userId") Long userId) {
        return new BaseRestResponse(userService.removeUser(userId));
    }


    /**
     * @Description: 重置密码
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:50
     */
    @ApiOperation("重置密码")
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('sys:user:rest')")
    public BaseRestResponse restPass(@PathVariable("userId") Long userId) {
        return new BaseRestResponse(userService.restPass(userId));
    }


    /**
     * @Description: 获取个人信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:50
     */
    @ApiOperation("获取用户个人信息")
    @GetMapping("/info")
    public BaseRestResponse getUserInfo() {
        return new BaseRestResponse(userService.findByUserInfoName(SecurityUtil.getUser().getUsername()));
    }


    /**
     * @Description: 修改密码
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:50
     */
    @ApiOperation("修改密码")
    @PutMapping("/updatePass")
    @PreAuthorize("hasAuthority('sys:user:updatePass')")
    public BaseRestResponse updatePass(@RequestParam String oldPass, @RequestParam String newPass) {
        // 校验密码流程
        SysUser sysUser = userService.findSecurityUserByUser(new SysUser().setUsername(SecurityUtil.getUser().getUsername()));
        if (!PreUtil.validatePass(oldPass, sysUser.getPassword())) {
            throw new BaseException("原密码错误");
        }
        if (StrUtil.equals(oldPass, newPass)) {
            throw new BaseException("新密码不能与旧密码相同");
        }
        // 修改密码流程
        SysUser user = new SysUser();
        user.setUserId(sysUser.getUserId());
        user.setPassword(PreUtil.encode(newPass));
        return new BaseRestResponse(userService.updateUserInfo(user));
    }

    /**
     * @Description: 修改监管所账号密码
     * @Author: Cohen
     * @date: 2022/1/27 14:25
     */
    @ApiOperation("监管(局)所账号密码")
    @PostMapping("/modifyPassword")
    public BaseRestResponse modifyPassword(@RequestBody UserDTO userDto) {
        if(ObjectUtil.isNull(userDto.getUserId()) || userDto.getUserId() == 0){
            return BaseRestResponse.error("账号不能为空");
        }
        SysUser sysUser = userService.findSecurityUserByUser(new SysUser().setUserId(userDto.getUserId()));
        sysUser.setPassword(PreUtil.encode(userDto.getPassword()));
        return new BaseRestResponse(userService.updateUserInfo(sysUser));
    }


    /**
     * @Description: 检测用户名是否存在 避免重复
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:50
     */
    @ApiOperation("检查用户是否存在")
    @PostMapping("/vailUserName")
    public BaseRestResponse vailUserName(@RequestParam String userName) {
        SysUser sysUser = userService.findSecurityUserByUser(new SysUser().setUsername(userName));
        return new BaseRestResponse(ObjectUtil.isNull(sysUser));
    }


}

