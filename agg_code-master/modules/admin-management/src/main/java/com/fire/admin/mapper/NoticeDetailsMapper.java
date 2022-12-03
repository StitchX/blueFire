package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.entity.NoticeDetailsInfo;
import com.fire.admin.dto.NoticeDetailsDto;
import com.fire.admin.vo.NoticeDetailsPercentVo;
import com.fire.admin.vo.NoticeDetailsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 通知详情mapper
 * @ClassName: NoticeDetailsMapper
 * @Author: liuliu
 * @Date: 2022/2/21 17:15
 */
@Repository
public interface NoticeDetailsMapper extends BaseMapper<NoticeDetailsInfo> {

    /**
     * 分页获取通知详情列表
     *
     * @param page
     * @param param
     */
    IPage<NoticeDetailsVo> noticeDetailsList(Page page, @Param("param") NoticeDetailsDto param);

    /**
     *@description:  获取通知详情的阅读率并分组
     * @param noticeIds
     *@return: java.util.List<com.fire.admin.vo.NoticeDetailsPercentVo>
     *@author: liuliu
     *@date: 2022-03-24 16:37
    */
    List<NoticeDetailsPercentVo> queryNoticeDetailPercent(List<Long> noticeIds);
}
