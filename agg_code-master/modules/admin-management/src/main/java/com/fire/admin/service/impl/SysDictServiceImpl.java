package com.fire.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.dto.DictDTO;
import com.fire.admin.mapper.SysDictMapper;
import com.fire.admin.service.ISysDictService;
import com.fire.dto.system.SysDict;
import com.fire.dto.system.SysDictItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * @author: liuliu
 * @ClassName: SysDictServiceImpl
 * @Description: 字典表 服务实现类
 * @date: 2021-05-13 19:08
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDict(DictDTO dictDto) {
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictDto, sysDict);
        return updateById(sysDict);
    }


    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public List<SysDictItem> queryDictItemByDictName(String dictName) {
        return baseMapper.queryDictItemByDictName(dictName);
    }
}
