package com.fire.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.LogInfoDto;
import com.fire.dto.log.LogInfo;

/**
 * @author: dw
 * @Description:
 * @date: 2022-01-24 13:32
 */
public interface LogInfoService extends IService<LogInfo> {

    /**
     * 根据条件分页查询操作日志
     *
     * @param
     * @return
     */
    IPage<LogInfo> getLogInfoByCondition(Page page, LogInfoDto param);

    /**
     * 操作日志入库
     *
     * @param logInfo
     */
    void saveLogInfo(LogInfo logInfo);

}
