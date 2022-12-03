package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.dto.LogInfoDto;
import com.fire.dto.log.LogInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @date: 2022-01-24 14:39
 */
public interface LogInfoMapper extends BaseMapper<LogInfo> {

    /**
     * 分页查询操作日志
     * @param param
     * @return
     */
    IPage<LogInfo> getLogInfoList(Page page, @Param("param") LogInfoDto param);

}
