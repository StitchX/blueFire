package com.fire.admin.rest;


import cn.hutool.core.util.ObjectUtil;
import com.fire.admin.dto.DeptDTO;
import com.fire.admin.service.ISysDeptService;
import com.fire.dto.response.BaseRestResponse;
import com.fire.dto.system.SysDept;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @classname: SysDeptController
 * @description 部门管理 前端控制器
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@RestController
@RequestMapping("/dept")
@Api(tags = "部门相关的接口")
@Slf4j
public class SysDeptController {

    @Autowired
    private ISysDeptService deptService;


    /**
     * @Description: 保存部门信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:33
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:dept:add')")
    public BaseRestResponse save(@RequestBody SysDept sysDept) {
        sysDept.setParentId(ObjectUtil.isEmpty(sysDept.getParentId()) ? 4L : sysDept.getParentId());
        return new BaseRestResponse(deptService.save(sysDept));
    }

    /**
     * @Description: 获取部门信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:33
     */
    @GetMapping
    @PreAuthorize("hasAuthority('sys:dept:view')")
    public BaseRestResponse getDeptList() {
        return new BaseRestResponse(deptService.selectDeptList());
    }

    /**
     * @Description: 获取部门树
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:33
     */
    @GetMapping("/tree")
    @ApiOperation(value = "部门树")
    public BaseRestResponse getDeptTree() {
        return new BaseRestResponse(deptService.getDeptTree());
    }


    /**
     * @Description: 更新部门信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:33
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:dept:update')")
    public BaseRestResponse update(@RequestBody DeptDTO deptDto) {
        return new BaseRestResponse(deptService.updateDeptById(deptDto));
    }

    /**
     * @Description: 根据id删除部门信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:34
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dept:delete')")
    public BaseRestResponse delete(@PathVariable("id") Long id) {
        return new BaseRestResponse(deptService.removeById(id));
    }

    @GetMapping("/id")
    @ApiOperation("获取本级部门以及子部门编号")
    public BaseRestResponse getDeptIds(Long id) {
        return new BaseRestResponse(deptService.selectDeptIds(id));
    }

}

