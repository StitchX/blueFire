package com.fire.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.NoticeDetailsDto;
import com.fire.admin.entity.NoticeDetailsInfo;
import com.fire.admin.vo.NoticeDetailsVo;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @date: 2022-02-21 22:37
 */
public interface NoticeDetailsService extends IService<NoticeDetailsInfo> {

    /**
     * 通知详情列表
     * @param noticeDetailsDto
     *
     */
    IPage<NoticeDetailsVo> noticeDetailsList(NoticeDetailsDto noticeDetailsDto);

    /**
     *@description:  批量新增通知详情
     * @param noticeDetailsInfoList
     *@return: boolean
     *@author: liuliu
     *@date: 2022-03-04 11:48
    */
    boolean saveNoticeDetailBatch(List<NoticeDetailsInfo> noticeDetailsInfoList);

    /**
     *@description:
     * @param noticeIds
     *@return: java.util.Map<java.lang.String,java.lang.Integer>
     *@author: liuliu
     *@date: 2022-03-24 17:19
    */
    Map<String,Integer> selectNoticeDetailPercent(List<Long> noticeIds);

}
