package com.fire.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.mapper.SysDictItemMapper;
import com.fire.admin.service.ISysDictItemService;
import com.fire.dto.system.SysDictItem;
import org.springframework.stereotype.Service;

/**
 * @classname: SysDictItemServiceImpl
 * @description 字典相关的服务实现
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {
}
