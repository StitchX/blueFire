package com.fire.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.dto.UserDTO;
import com.fire.admin.mapper.SysUserMapper;
import com.fire.admin.service.*;
import com.fire.admin.task.SupervisionBureauData;
import com.fire.admin.util.*;
import com.fire.common.exception.BaseException;
import com.fire.dto.log.LogInfo;
import com.fire.dto.system.SysUser;
import com.fire.dto.system.SysUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * @classname: SysUserServiceImpl
 * @description 用户表 服务实现类
 * @author: liu liu
 * @create: 2020-08-13 14:51
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SupervisionBureauService supervisionBureauService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LogInfoService logInfoService;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    private static ConcurrentHashMap<String, SysUser> authorityUserMap = new ConcurrentHashMap();

    /**
     * @Description: 分页查询用户信息（含有角色信息）
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    @Override
    public IPage<SysUser> getUsersWithRolePage(Page page, UserDTO userDTO) {
        if (userDTO != null && userDTO.getSupervisionId() != null) {
            userDTO.setSupervisionIdList(supervisionBureauService.querySupervisionBureau(userDTO.getSupervisionId()));
        }
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * @Description: 保存用户以及角色部门等信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        // 默认密码 123456
        sysUser.setPassword(PreUtil.encode("123456"));
        if (ObjectUtil.isEmpty(sysUser.getSupervisionId())) {
            throw new BaseException("请绑定监管所或监管局");
        }
        baseMapper.insertUser(sysUser);
        List<SysUserRole> userRoles = userDto.getRoleList().stream().map(item -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(item);
            sysUserRole.setUserId(sysUser.getUserId());
            return sysUserRole;
        }).collect(Collectors.toList());

        return userRoleService.saveBatch(userRoles);
    }

    /**
     * @Description: 更新用户以及角色部门等信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        baseMapper.updateById(sysUser);
        userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, sysUser.getUserId()));
        List<SysUserRole> userRoles = userDto.getRoleList().stream().map(item -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(item);
            sysUserRole.setUserId(sysUser.getUserId());
            return sysUserRole;
        }).collect(Collectors.toList());

        return userRoleService.saveBatch(userRoles);
    }

    /**
     * @Description: 删除用户信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUser(Long userId) {
        baseMapper.deleteById(userId);
        return userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
    }

    /**
     * @Description: 重置密码
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    @Override
    public boolean restPass(Long userId) {
        return baseMapper.updateById(new SysUser().setPassword("123456").setUserId(userId)) > 0;
    }

    /**
     * @Description: 通过用户名查找用户个人信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:51
     */
    @Override
    public SysUser findByUserInfoName(String username) {
        SysUser sysUser = baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery().select(SysUser::getUserId, SysUser::getUsername, SysUser::getPhone, SysUser::getEmail, SysUser::getPassword, SysUser::getSupervisionId, SysUser::getJobId, SysUser::getAvatar).eq(SysUser::getUsername, username));
        // 获取部门
        sysUser.setSupervisionName(SupervisionBureauData.getSupervisionBureauName(sysUser.getSupervisionId()));
        // 获取岗位
//        sysUser.setJobName(jobService.selectJobNameByJobId(sysUser.getJobId()));
        return sysUser;
    }

    /**
     * @Description: 根据用户id查询权限
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    @Override
    public Set<String> findPermsByUserId(Long userId) {
        return menuService.findPermsByUserId(userId).stream().filter(StrUtil::isNotEmpty).collect(Collectors.toSet());
    }

    /**
     * @Description: 通过用户id查询角色集合
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    @Override
    public Set<String> findRoleIdByUserId(Long userId) {
        return userRoleService.selectUserRoleListByUserId(userId).stream().map(sysUserRole -> "ROLE_" + sysUserRole.getRoleId()).collect(Collectors.toSet());
    }


    /**
     * @Description: 账户密码登录
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    @Override
    public String login(String username, String password, HttpServletRequest request) {
        //用户验证
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        log.info("用户认证信息为：【{}】", JSONUtil.parse(authentication.getPrincipal()));
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        PreSecurityUser userDetail = (PreSecurityUser) authentication.getPrincipal();

        return JwtUtil.generateToken(userDetail);
    }

    /**
     * @Description: 注册用户
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean register(UserDTO userDTO) {
        // 查询用户名是否存在
        SysUser byUserInfoName = findSecurityUser(userDTO.getUsername());
        if (ObjectUtil.isNotNull(byUserInfoName)) {
            throw new BaseException("账户名已被注册");
        }
        SysUser securityUser = findSecurityUser(userDTO.getPhone());
        if (ObjectUtil.isNotNull(securityUser)) {
            throw new BaseException("手机号已被注册");
        }
        userDTO.setSupervisionId(6L);
        userDTO.setLockFlag("0");
        SysUser sysUser = new SysUser();
        // 对象拷贝
        BeanUtil.copyProperties(userDTO, sysUser);
        // 加密后的密码
        sysUser.setPassword(PreUtil.encode(userDTO.getPassword()));
        baseMapper.insertUser(sysUser);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(14L);
        sysUserRole.setUserId(sysUser.getUserId());
        return userRoleService.save(sysUserRole);
    }

    /**
     * @Description: 修改用户信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    @Override
    public boolean updateUserInfo(SysUser sysUser) {
        String username = SecurityUtil.getUser().getUsername();
        boolean result = baseMapper.updateById(sysUser) > 0;

        StringBuilder desc = new StringBuilder();
        try {
            if (result) {
                desc.append("成功");
            } else {
                desc.append("失败");
            }
        } catch (Throwable throwable) {
            desc.append("失败");
        } finally {
            LogInfo loginLogInfo = LogInfo.builder()
                    //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                    .name(username)
                    .content(username + "在" + DateUtil.formatLocalDateTime(LocalDateTime.now()).concat("修改" + supervisionBureauService.getSupervisionBureauById(sysUser.getSupervisionId()).getSupervisionName() + "账号密码").concat(desc.toString()))
                    .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                    .operateTime(new Timestamp(System.currentTimeMillis()))
                    .module("系统")
                    .build();

            logInfoService.saveLogInfo(loginLogInfo);
        }

        return result;
    }


    /**
     * @Description: 通过用户去查找用户(id / 用户名 / 手机号)
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:52
     */
    @Override
    public SysUser findSecurityUserByUser(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery().select(SysUser::getUserId, SysUser::getUsername, SysUser::getPassword, SysUser::getSupervisionId, SysUser::getLockFlag);
        if (StrUtil.isNotEmpty(sysUser.getUsername())) {
            select.eq(SysUser::getUsername, sysUser.getUsername());
        } else if (StrUtil.isNotEmpty(sysUser.getPhone())) {
            select.eq(SysUser::getPhone, sysUser.getPhone());
        } else if (ObjectUtil.isNotNull(sysUser.getUserId()) && sysUser.getUserId() != 0) {
            select.eq(SysUser::getUserId, sysUser.getUserId());
        }
        SysUser user = baseMapper.selectOne(select);
        return user;
    }

    @Override
    public Set<Long> findSupervisionBureaus(Long supervisionBureauId) {
        return supervisionBureauService.querySupervisionBureau(supervisionBureauId);
    }


    private SysUser findSecurityUser(String userIdOrUserNameOrPhone) {
        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery().select(SysUser::getUserId, SysUser::getUsername, SysUser::getPassword).eq(SysUser::getUsername, userIdOrUserNameOrPhone).or().eq(SysUser::getPhone, userIdOrUserNameOrPhone).or().eq(SysUser::getUserId, userIdOrUserNameOrPhone);
        return baseMapper.selectOne(select);
    }


    /**
     * @descible: 获取所有未删除的用户信息，用于缓存实现mybatis 拦截器
     * @param:
     * @return: java.util.List<com.fire.dto.system.SysUser>
     * @author: liuliu
     * @date: 2021-06-29 11:21
     */
    public ConcurrentHashMap<String, SysUser> getUsersAuthorityAll() {
        List<SysUser> sysUsers = baseMapper.queryUsersAuthority();
        sysUsers.forEach(user -> {
            authorityUserMap.put(user.getUserId().toString().concat(user.getUsername()), user);
        });
        return authorityUserMap;
    }

}
