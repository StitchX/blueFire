package com.fire.admin.rest;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.dto.DictDTO;
import com.fire.admin.service.ISysDictService;
import com.fire.dto.response.BaseRestResponse;
import com.fire.dto.system.SysDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @author: liuliu
 * @ClassName: SysDictController
 * @Description: 字典，前端控制器
 * @date: 2021-05-13 18:08
 */
@RestController
@RequestMapping("/dict")
public class SysDictController {

    @Autowired
    private ISysDictService dictService;


    /**
     * @Description: 添加字典信息
     * @Param: [sysDict]
     * @return: com.fire.admin.util.R
     * @Author: liuliu
     * @Date: 2021/5/13 18:09
     */
    @PreAuthorize("hasAuthority('sys:dict:add')")
    @PostMapping
    public BaseRestResponse add(@RequestBody SysDict sysDict) {
        return new BaseRestResponse(dictService.save(sysDict));
    }


    /**
     * @Description: 获取字典列表集合
     * @Param: [page, sysDict]
     * @return: com.fire.admin.util.R
     * @Author: liuliu
     * @Date: 2021/5/13 18:09
     */
    @GetMapping
    @PreAuthorize("hasAuthority('sys:dipt:view')")
    public BaseRestResponse getList(Page page, SysDict sysDict) {
        return new BaseRestResponse(dictService.page(page, Wrappers.query(sysDict)));
    }


    /**
     * 更新字典
     *
     * @param dictDto
     * @return
     */
    @PreAuthorize("hasAuthority('sys:dict:edit')")
    @PutMapping
    public BaseRestResponse update(@RequestBody DictDTO dictDto) {
        return new BaseRestResponse(dictService.updateDict(dictDto));
    }


    /**
     * 根据id删除字典
     *
     * @param id
     * @return //
     */
    @PreAuthorize("hasAuthority('sys:dict:del')")
    @DeleteMapping("{id}")
    public BaseRestResponse delete(@PathVariable("id") int id) {
        return new BaseRestResponse(dictService.removeById(id));
    }


    /**
     * 根据字典名称查询字段详情
     *
     * @param dictName
     * @return
     */
    @GetMapping("/queryDictItemByDictName/{dictName}")
    public BaseRestResponse queryDictItemByDictName(@PathVariable("dictName") String dictName) {
        return new BaseRestResponse(dictService.queryDictItemByDictName(dictName));
    }

}

