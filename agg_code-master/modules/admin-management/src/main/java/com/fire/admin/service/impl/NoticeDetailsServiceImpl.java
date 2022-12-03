package com.fire.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.entity.NoticeDetailsInfo;
import com.fire.admin.mapper.NoticeDetailsMapper;
import com.fire.admin.dto.NoticeDetailsDto;
import com.fire.admin.service.NoticeDetailsService;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.util.SecurityUtil;
import com.fire.admin.vo.NoticeDetailsPercentVo;
import com.fire.admin.vo.NoticeDetailsVo;
import com.fire.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description:
 * @date: 2022-02-21 22:54
 * @Modified By:
 */
@Service
@Slf4j
public class NoticeDetailsServiceImpl extends ServiceImpl<NoticeDetailsMapper, NoticeDetailsInfo> implements NoticeDetailsService {

    @Override
    public IPage<NoticeDetailsVo> noticeDetailsList(NoticeDetailsDto noticeDetailsDto) {

        PreSecurityUser user = SecurityUtil.getUser();
        SecurityUtil.getUser().getSupervisionIds().add(user.getSupervisionId());
        Set<Long> supervisionIds = user.getSupervisionIds();
        noticeDetailsDto.setSupervisionIds(supervisionIds);
        Page<NoticeDetailsVo> page = new Page<>();
        if (ObjectUtil.isNotEmpty(noticeDetailsDto.getCurrent()) && ObjectUtil.isNotEmpty(noticeDetailsDto.getSize())) {
            page.setCurrent(noticeDetailsDto.getCurrent());
            page.setSize(noticeDetailsDto.getSize());
        }
        log.info("查询通知详情的参数为：{}", JSONUtil.parseObj(noticeDetailsDto));
        return baseMapper.noticeDetailsList(page, noticeDetailsDto);
    }

    /**
     * @param noticeDetailsInfoList
     * @description: 批量新增通知详情
     * @return: boolean
     * @author: liuliu
     * @date: 2022-03-04 11:49
     */
    @Override
    public boolean saveNoticeDetailBatch(List<NoticeDetailsInfo> noticeDetailsInfoList) {
        if (CollectionUtil.isEmpty(noticeDetailsInfoList)) {
            throw new BaseException("批量新增通知详情失败");
        }
        return this.saveBatch(noticeDetailsInfoList, 200);
    }

    @Override
    public Map<String, Integer> selectNoticeDetailPercent(List<Long> noticeIds) {
        List<NoticeDetailsPercentVo> percentVos = baseMapper.queryNoticeDetailPercent(noticeIds);
        if (CollectionUtil.isEmpty(percentVos)) {
            return null;
        }
        Map<String, Integer> map = new HashMap<>(16);
        percentVos.forEach(v -> map.put(v.getNoticeId().toString().concat("_").concat(v.getReadStatus().toString()), v.getNum()));
        return map;
    }


}
