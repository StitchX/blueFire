package com.blue.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.domain.NoticeDetailsInfo;
import com.blue.business.dto.NoticeDto;
import com.blue.business.mapper.NoticeDetailsMapper;
import com.blue.business.service.NoticeDetailsService;
import com.fire.common.exception.BaseException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * @Description:
 * @ClassName: NoticeDetailsServiceImpl
 * @Author: liuliu
 * @Date: 2022/3/24 11:41
 */
@Service
public class NoticeDetailsServiceImpl extends ServiceImpl<NoticeDetailsMapper, NoticeDetailsInfo> implements NoticeDetailsService {
    @Override
    public boolean updateNoticeDetails(NoticeDto noticeDto) {
        if (ObjectUtil.isNull(noticeDto) || ObjectUtil.isNull(noticeDto.getNoticeDetailsId())) {
            throw new BaseException("参数错误");
        }
        NoticeDetailsInfo build = NoticeDetailsInfo.builder()
                .noticeDetailsId(noticeDto.getNoticeDetailsId())
                .msgReadTime(new Timestamp(System.currentTimeMillis())).build();
        boolean flag = this.updateById(build);
        if (flag) {
            // TODO 发送MQ消息
        }
        return flag;
    }
}
