package com.blue.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blue.business.domain.NoticeInfo;
import com.blue.business.dto.NoticeDto;
import com.blue.business.vo.NoticeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 通知
 * @ClassName: NoticeMapper
 * @Author: liuliu
 * @Date: 2022/3/23 16:35
 */
@Mapper
public interface NoticeMapper extends BaseMapper<NoticeInfo> {

    /**
     * @param page
     * @param merchantId
     * @description: 商户获取通知列表
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.blue.business.vo.NoticeVo>
     * @author: liuliu
     * @date: 2022-03-23 17:53
     */
    IPage<NoticeVo> queryNoticePageInfo(Page<NoticeVo> page, Long merchantId);

    /**
     * @param noticeDto
     * @description: 商户获取通知详情
     * @return: com.blue.business.vo.NoticeVo
     * @author: liuliu
     * @date: 2022-03-23 17:53
     */
    NoticeVo queryNoticeDetails(@Param("param") NoticeDto noticeDto);

}
