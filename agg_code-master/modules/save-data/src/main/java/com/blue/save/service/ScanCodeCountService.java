package com.blue.save.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.dto.entity.ScanCodeCount;

public interface ScanCodeCountService extends IService<ScanCodeCount> {

    boolean insert(ScanCodeCount scanCodeCount);
}
