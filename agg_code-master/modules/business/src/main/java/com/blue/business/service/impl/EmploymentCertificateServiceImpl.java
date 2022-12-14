package com.blue.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.domain.EmploymentCertificate;
import com.blue.business.dto.EmploymentCertificateDto;
import com.blue.business.mapper.EmploymentCertificateMapper;
import com.blue.business.service.EmploymentCertificateService;
import com.blue.business.util.CheckMobilePhoneUtil;
import com.blue.business.util.IDCardUtil;
import com.blue.business.vo.EmploymentCertificateVo;
import com.fire.common.exception.BaseException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @date: 2022-03-21 10:51
 */
@Service
@Slf4j
public class EmploymentCertificateServiceImpl extends ServiceImpl<EmploymentCertificateMapper, EmploymentCertificate> implements EmploymentCertificateService {

    @Override
    public List<EmploymentCertificateVo> employmentCertificateList(EmploymentCertificateDto dto) {

        Page<EmploymentCertificate> page = new Page();
        List<EmploymentCertificateVo> vos = Lists.newArrayList();

        if (ObjectUtil.isNotEmpty(dto.getCurrent()) && ObjectUtil.isNotEmpty(dto.getSize())) {
            page.setCurrent(dto.getCurrent());
            page.setSize(dto.getSize());
        }

        LambdaQueryWrapper<EmploymentCertificate> wrapper = new LambdaQueryWrapper();

        wrapper.select(EmploymentCertificate.class, field -> {
            String column = field.getColumn();
            return !"merchant_id".equals(column) && !"create_time".equals(column) && !"is_deleted".equals(column);
        }).orderByDesc(EmploymentCertificate::getCreateTime);

        Page<EmploymentCertificate> employmentCertificatePage = null;

        try {
            employmentCertificatePage = baseMapper.selectPage(page, wrapper);
        } catch (Exception e) {
            log.error("???????????????????????????!", e);
        }

        List<EmploymentCertificate> records = employmentCertificatePage.getRecords();

        if (CollectionUtil.isNotEmpty(records)) {
            records.forEach(employmentCertificate -> {
                EmploymentCertificateVo vo = EmploymentCertificateVo.builder()
                        .id(employmentCertificate.getId())
                        .imageUrl(employmentCertificate.getImageUrl())
                        .name(employmentCertificate.getName())
                        .phone(DesensitizedUtil.mobilePhone(employmentCertificate.getPhone()))
                        .identityCard(DesensitizedUtil.idCardNum(employmentCertificate.getIdentityCard(), 6, 2))
                        .type(employmentCertificate.getType())
                        .receiveDate(employmentCertificate.getReceiveDate())
                        .validPeriod(employmentCertificate.getValidPeriod())
                        .build();

                vos.add(vo);
            });
        }
        return vos;
    }

    @Override
    public int addEmploymentCertificate(EmploymentCertificateDto dto) {

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

            if (ObjectUtil.isEmpty(dto.getType())) {
                throw new BaseException("????????????????????????!");
            }

            if (ObjectUtil.isEmpty(dto.getType())) {
                throw new BaseException("????????????????????????!");
            }

            if (dto.getType().length() > 16) {
                throw new BaseException("??????????????????????????????!");
            }

            if (ObjectUtil.isEmpty(dto.getReceiveDate())) {
                throw new BaseException("????????????????????????!");
            }

            if (ObjectUtil.isEmpty(dto.getValidPeriod())) {
                throw new BaseException("?????????????????????!");
            }

            EmploymentCertificate employmentCertificate = EmploymentCertificate.builder()
                    .imageUrl(dto.getImageUrl().trim())
                    .name(dto.getName().trim())
                    .phone(dto.getPhone().trim())
                    .identityCard(dto.getIdentityCard().trim())
                    .type(dto.getType().trim())
                    .receiveDate(dto.getReceiveDate())
                    .validPeriod(dto.getValidPeriod())
                    .build();

            int res = 0;

            try {
                res = baseMapper.insert(employmentCertificate);
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
    public int removeEmploymentCertificate(Long id) {

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
