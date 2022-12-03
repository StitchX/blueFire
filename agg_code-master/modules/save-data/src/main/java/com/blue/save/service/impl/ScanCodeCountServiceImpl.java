package com.blue.save.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.dto.entity.ScanCodeCount;
import com.blue.save.mapper.ScanCodeCountMapper;
import com.blue.save.service.ScanCodeCountService;
import org.springframework.stereotype.Service;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 扫码记录
 * @date 2022/3/10 10:31
 */
@Service
public class ScanCodeCountServiceImpl extends ServiceImpl<ScanCodeCountMapper, ScanCodeCount> implements ScanCodeCountService {
    @Override
    public boolean insert(ScanCodeCount scanCodeCount) {
        return retBool(baseMapper.insert(scanCodeCount));
    }
}
