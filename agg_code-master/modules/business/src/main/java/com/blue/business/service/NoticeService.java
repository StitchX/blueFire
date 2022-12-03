package com.blue.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.business.domain.NoticeInfo;
import com.blue.business.dto.NoticeDto;
import com.blue.business.vo.NoticeVo;

/**
 * @Description:
 * @ClassName: NoticeService
 * @Author: liuliu
 * @Date: 2022/3/23 16:54
 */
public interface NoticeService extends IService<NoticeInfo> {

    /**
     * @param noticeDto
     * @description: 商户获取通知分页数据
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.blue.business.vo.NoticeVo>
     * @author: liuliu
     * @date: 2022-03-23 17:50
     */
    IPage<NoticeVo> selectNoticePageInfo(NoticeDto noticeDto);

    /**
     *@description:  获取通知详情
     * @param noticeDto
     *@return: com.blue.business.vo.NoticeVo
     *@author: liuliu
     *@date: 2022-03-23 17:52
    */
    NoticeVo selectNoticeDetails(NoticeDto noticeDto);


}
