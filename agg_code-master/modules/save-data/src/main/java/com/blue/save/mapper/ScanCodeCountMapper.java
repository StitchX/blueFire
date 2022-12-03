package com.blue.save.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.dto.entity.ScanCodeCount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商户扫码(ScanCodeCount)表数据库访问层
 *
 * @author ZJQ  2022-02-28 15:12:56
 */

@Mapper
public interface ScanCodeCountMapper extends BaseMapper<ScanCodeCount> {

    int insert(ScanCodeCount scanCodeCount);
}

