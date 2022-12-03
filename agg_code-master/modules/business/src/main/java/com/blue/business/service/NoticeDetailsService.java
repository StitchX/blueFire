package com.blue.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.business.domain.NoticeDetailsInfo;
import com.blue.business.dto.NoticeDto;

/**
 * @Description:
 * @ClassName: NoticeDetailsService
 * @Author: liuliu
 * @Date: 2022/3/24 11:41
 */
public interface NoticeDetailsService extends IService<NoticeDetailsInfo> {

    boolean updateNoticeDetails(NoticeDto noticeDto);

}
