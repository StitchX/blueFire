package com.fire.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.mapper.LogInfoMapper;
import com.fire.admin.dto.LogInfoDto;
import com.fire.admin.service.LogInfoService;
import com.fire.admin.util.SecurityUtil;
import com.fire.dto.log.LogInfo;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author: dw
 * @Description:
 * @date: 2022-01-24 13:34
 */
@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements LogInfoService {

    /**
     * 根据条件查询日志记录信息带分页
     *
     * @param param
     * @return
     */
    @Override
    public IPage<LogInfo> getLogInfoByCondition(Page page, LogInfoDto param) {
        Set<Long> supervisionIds = SecurityUtil.getUser().getSupervisionIds();
        supervisionIds.add(SecurityUtil.getUser().getSupervisionId());

        param.setSupervisionIds(supervisionIds);

        Page<LogInfo> page1 = new Page<>();

        if (ObjectUtil.isNotEmpty(param.getCurrent()) && ObjectUtil.isNotEmpty(param.getSize())) {
            page1.setCurrent(param.getCurrent());
            page1.setSize(param.getSize());
        }
        return baseMapper.getLogInfoList(page1, param);
    }

    /**
     * 日志记录信息入库
     *
     * @param logInfo
     */
    @Override
    public void saveLogInfo(LogInfo logInfo) {
        baseMapper.insert(logInfo);
    }
}
