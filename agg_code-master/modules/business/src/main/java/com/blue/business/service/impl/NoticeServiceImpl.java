package com.blue.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.domain.NoticeDetailsInfo;
import com.blue.business.domain.NoticeInfo;
import com.blue.business.dto.NoticeDto;
import com.blue.business.mapper.NoticeMapper;
import com.blue.business.service.NoticeDetailsService;
import com.blue.business.service.NoticeService;
import com.blue.business.util.NoticeReadFlagEnum;
import com.blue.business.vo.NoticeVo;
import com.fire.common.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description:
 * @ClassName: NoticeServiceImpl
 * @Author: liuliu
 * @Date: 2022/3/23 16:55
 */
@Service
@AllArgsConstructor
@Slf4j
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeInfo> implements NoticeService {

    private NoticeDetailsService noticeDetailsService;

    @Override
    public IPage<NoticeVo> selectNoticePageInfo(NoticeDto noticeDto) {
        Page<NoticeVo> page = new Page<>();
        if (ObjectUtil.isNotNull(noticeDto) && ObjectUtil.isNotNull(noticeDto.getCurrent()) && ObjectUtil.isNotNull(noticeDto.getSize())) {
            page.setCurrent(noticeDto.getCurrent());
            page.setSize(noticeDto.getSize());
        }
        /* Long merchantId = SecurityUtil.getUser().getMerchantId();*/
        Long merchantId = 96L;
        if (ObjectUtil.isNull(merchantId) || merchantId == 0L) {
            throw new BaseException("请联系管理员");
        }
        return baseMapper.queryNoticePageInfo(page, merchantId);
    }


    @Override
    public NoticeVo selectNoticeDetails(NoticeDto noticeDto) {
        long time1 = System.currentTimeMillis();

        if (ObjectUtil.isNull(noticeDto.getNoticeId()) || ObjectUtil.isNull(noticeDto.getNoticeDetailsId()) || ObjectUtil.isNull(noticeDto.getReadFlag())) {
            throw new BaseException("请求参数有误");
        }
        NoticeVo noticeVo = baseMapper.queryNoticeDetails(noticeDto);
        if (ObjectUtil.isNull(noticeVo)) {
            return null;
        }
        noticeVo.setNoticeDetailsId(noticeDto.getNoticeDetailsId());
        if (StrUtil.isNotBlank(noticeVo.getFile())) {
            List<NoticeVo.AdjuncFile> adjuncFiles = JSONUtil.toList(noticeVo.getFile(), NoticeVo.AdjuncFile.class);
            noticeVo.setFile(null);
            noticeVo.setAdjunctFile(adjuncFiles);
        }
        // 处理通知详情
        noticeDetailsUpdate(noticeDto);
        long time2 = System.currentTimeMillis();
        log.info("调用通知详情接口用时为：{}", time2 - time1);
        return noticeVo;
    }

    private void noticeDetailsUpdate(NoticeDto noticeDto) {
        // 默认情况下为非必读，查看详情的时候就写入阅读时间跟送达时间
        if (NoticeReadFlagEnum.DEFAULT_READ_FLAG.getReadCode().equals(noticeDto.getReadFlag())) {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            NoticeDetailsInfo detailsInfo = NoticeDetailsInfo.builder()
                    .noticeDetailsId(noticeDto.getNoticeDetailsId())
                    .msgReadTime(time)
                    .msgDeliveryTime(time).build();
            noticeDetailsService.updateById(detailsInfo);
        }
        if (NoticeReadFlagEnum.READ_FLAG.getReadCode().equals(noticeDto.getReadFlag())) {
            Timestamp time = new Timestamp(System.currentTimeMillis());
            NoticeDetailsInfo detailsInfo = NoticeDetailsInfo.builder()
                    .noticeDetailsId(noticeDto.getNoticeDetailsId())
                    .msgDeliveryTime(time).build();
            noticeDetailsService.updateById(detailsInfo);
        }
    }
}
