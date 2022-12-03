package com.blue.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.business.domain.NoticeDetailsInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 商户通知详情
 * @ClassName: NoticeDetailsMapper
 * @Author: liuliu
 * @Date: 2022/3/23 16:36
 */
@Mapper
public interface NoticeDetailsMapper extends BaseMapper<NoticeDetailsInfo> {
}
