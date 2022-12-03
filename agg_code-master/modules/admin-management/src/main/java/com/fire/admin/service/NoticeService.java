package com.fire.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.NoticeDto;
import com.fire.admin.dto.NoticeScheduleExecuteDto;
import com.fire.admin.entity.NoticeInfo;
import com.fire.admin.vo.NotiDetaVo;
import com.fire.admin.vo.NoticeVo;

/**
 * @author: admin
 * @Description:
 * @date: 2022-02-21 22:08
 * @Modified By:
 */
public interface NoticeService extends IService<NoticeInfo> {

    /**
     * 通知列表
     *
     * @param param
     * @return
     */
    IPage<NoticeVo> noticeList(NoticeDto param);

    /**
     * @param noticeDto
     * @description: 新增通知
     * @return: void
     * @author: liuliu
     * @date: 2022-02-23 15:40
     */
    boolean insertNotice(NoticeDto noticeDto);


    /**
     * 通知逻辑删除
     *
     * @param noticeDto
     */
    int deleteNoticeById(NoticeDto noticeDto);

    /**
     * 根据通知编号获取通知详情
     *
     * @param noticeDto
     */
    NotiDetaVo queryNoticeDetailsById(NoticeDto noticeDto);

    /**
     * @param noticeDto
     * @description: 修改通知
     * @return: boolean
     * @author: liuliu
     * @date: 2022-03-01 11:31
     */
    boolean updateNotice(NoticeDto noticeDto);


    /**
     * @param noticeId
     * @description: 通过通知编号获取需要发送消息的商户
     * @return: com.fire.admin.dto.NoticeDto.NoticeCondition
     * @author: liuliu
     * @date: 2022-03-04 11:40
     */
    NoticeScheduleExecuteDto querySendNoticeById(Long noticeId);

}
