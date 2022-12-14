package com.blue.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.domain.HealthCertificate;
import com.blue.business.dto.HealthCertificateDto;
import com.blue.business.mapper.HealthCertificateMapper;
import com.blue.business.service.HealthCertificateService;
import com.blue.business.util.CheckMobilePhoneUtil;
import com.blue.business.util.IDCardUtil;
import com.blue.business.vo.HealthCertificateVo;
import com.fire.common.exception.BaseException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @date: 2022-03-21 10:53
 */
@Service
@Slf4j
public class HealthCertificateServiceImpl extends ServiceImpl<HealthCertificateMapper, HealthCertificate> implements HealthCertificateService {

    @Override
    public List<HealthCertificateVo> healthCertificateList(HealthCertificateDto dto) {

        Page<HealthCertificate> page = new Page();
        List<HealthCertificateVo> vos = Lists.newArrayList();

        if (ObjectUtil.isNotEmpty(dto.getCurrent()) && ObjectUtil.isNotEmpty(dto.getSize())) {
            page.setCurrent(dto.getCurrent());
            page.setSize(dto.getSize());
        }

        LambdaQueryWrapper<HealthCertificate> wrapper = new LambdaQueryWrapper();

        wrapper.select(HealthCertificate.class, field -> {
            String column = field.getColumn();
            return !"merchant_id".equals(column) && !"create_time".equals(column) && !"is_deleted".equals(column) && !"update_time".equals(column);
        }).orderByDesc(HealthCertificate::getCreateTime);

        Page<HealthCertificate> healthCertificatePage = null;

        try {
            healthCertificatePage = baseMapper.selectPage(page, wrapper);
        } catch (Exception e) {
            log.error("???????????????????????????!", e);
        }

        List<HealthCertificate> records = healthCertificatePage.getRecords();

        if (CollectionUtil.isNotEmpty(records)) {
            records.forEach(healthCertificate -> {
                HealthCertificateVo vo = HealthCertificateVo.builder()
                        .id(healthCertificate.getId())
                        .imageUrl(healthCertificate.getImageUrl())
                        .name(healthCertificate.getName())
                        .phone(DesensitizedUtil.mobilePhone(healthCertificate.getPhone()))
                        .identityCard(DesensitizedUtil.idCardNum(healthCertificate.getIdentityCard(), 6, 2))
                        .position(healthCertificate.getPosition())
                        .receiveDate(healthCertificate.getReceiveDate())
                        .validPeriod(healthCertificate.getValidPeriod())
                        .build();

                vos.add(vo);
            });
        }
        return vos;
    }

    @Override
    public int addHealthCertificate(HealthCertificateDto dto) {

        if (ObjectUtil.isNotEmpty(dto)) {
            if (ObjectUtil.isEmpty(dto.getImageUrl())) {
                throw new BaseException("????????????????????????!");
            }

            if (ObjectUtil.isEmpty(dto.getName())) {
                throw new BaseException("??????????????????!");
            }

            if (dto.getName().length() > 16) {
                throw new BaseException("????????????????????????!");
            }

            if (!CheckMobilePhoneUtil.CheckMobilePhone(dto.getPhone())) {
                throw new BaseException("??????????????????,?????????!");
            }

            if (!IDCardUtil.checkIdCard(dto.getIdentityCard())) {
                throw new BaseException("?????????????????????,?????????!");
            }

            if (ObjectUtil.isEmpty(dto.getPosition())) {
                throw new BaseException("??????????????????!");
            }

            if (dto.getPosition().length() > 16) {
                throw new BaseException("????????????????????????!");
            }

            if (ObjectUtil.isEmpty(dto.getReceiveDate())) {
                throw new BaseException("???????????????????????????");
            }

            if (ObjectUtil.isEmpty(dto.getValidPeriod())) {
                throw new BaseException("?????????????????????!");
            }

            HealthCertificate healthCertificate = HealthCertificate.builder()
                    .imageUrl(dto.getImageUrl().trim())
                    .name(dto.getName().trim())
                    .phone(dto.getPhone().trim())
                    .identityCard(dto.getIdentityCard().trim())
                    .position(dto.getPosition().trim())
                    .receiveDate(dto.getReceiveDate())
                    .validPeriod(dto.getValidPeriod())
                    .build();

            int res = 0;

            try {
                res = baseMapper.insert(healthCertificate);
            } catch (Exception e) {
                log.error("?????????????????????!", e);
            }

            if (res > 0) {
                return res;
            } else {
                throw new BaseException("????????????!");
            }

        }

        throw new BaseException("????????????!");
    }

    @Override
    public int removeHealthCertificate(Long id) {

        int res = 0;

        try {
            res = baseMapper.deleteById(id);
        } catch (Exception e) {
            log.error("?????????????????????!", e);
        }

        if (res > 0) {
            return res;
        } else {
            throw new BaseException("????????????!");
        }
    }
}
