package com.fire.admin.rest;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.service.ISysDictItemService;
import com.fire.dto.response.BaseRestResponse;
import com.fire.dto.system.SysDictItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



/**
 * @classname: SysDictItemController
 * @description 字典相关的接口
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@RestController
@RequestMapping("/dictItem")
public class SysDictItemController {


    @Autowired
    private ISysDictItemService dictItemService;

    /**
     * @Description: 分页查询字典详情内容
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:39
     */
    @GetMapping
    public BaseRestResponse getDictItemPage(Page page, SysDictItem sysDictItem) {
        return new BaseRestResponse(dictItemService.page(page, Wrappers.query(sysDictItem)));
    }

    /**
     * @Description: 添加字典详情
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:39
     */
    @PreAuthorize("hasAuthority('sys:dictItem:add')")
    @PostMapping
    public BaseRestResponse add(@RequestBody SysDictItem sysDictItem) {
        return new BaseRestResponse(dictItemService.save(sysDictItem));
    }

    /**
     * @Description: 更新字典详情
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:39
     */
    @PreAuthorize("hasAuthority('sys:dictItem:edit')")
    @PutMapping
    public BaseRestResponse update(@RequestBody SysDictItem sysDictItem) {
        return new BaseRestResponse(dictItemService.updateById(sysDictItem));
    }

    /**
     * @Description: 删除字典详情
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:39
     */
    @PreAuthorize("hasAuthority('sys:dictItem:del')")
    @DeleteMapping("/{id}")
    public BaseRestResponse delete(@PathVariable("id") String id) {
        return new BaseRestResponse(dictItemService.removeById(id));
    }


}
