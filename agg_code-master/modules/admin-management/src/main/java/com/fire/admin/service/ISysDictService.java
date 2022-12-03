package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.DictDTO;
import com.fire.dto.system.SysDict;
import com.fire.dto.system.SysDictItem;

import java.io.Serializable;
import java.util.List;


/**
 * @author: liuliu
 * @ClassName: ISysDictService
 * @Description: 字典表 服务类
 * @date: 2021-05-13 19:08
 */
public interface ISysDictService extends IService<SysDict> {

    /**
     * 修改字典
     *
     * @param dictDto
     * @return
     */
    boolean updateDict(DictDTO dictDto);


    /**
     * 根据主键Id删除字典
     *
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 根据字典名称查询字段详情
     *
     * @param dictName
     * @return
     */
    List<SysDictItem> queryDictItemByDictName(String dictName);
}
