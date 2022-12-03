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
            log.error("健康证列表查询异常!", e);
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
                throw new BaseException("图片地址不能为空!");
            }

            if (ObjectUtil.isEmpty(dto.getName())) {
                throw new BaseException("姓名不能为空!");
            }

            if (dto.getName().length() > 16) {
                throw new BaseException("姓名长度超出限制!");
            }

            if (!CheckMobilePhoneUtil.CheckMobilePhone(dto.getPhone())) {
                throw new BaseException("电话输入错误,请重试!");
            }

            if (!IDCardUtil.checkIdCard(dto.getIdentityCard())) {
                throw new BaseException("身份证输入错误,请重试!");
            }

            if (ObjectUtil.isEmpty(dto.getPosition())) {
                throw new BaseException("职位不能为空!");
            }

            if (dto.getPosition().length() > 16) {
                throw new BaseException("职位长度超出限制!");
            }

            if (ObjectUtil.isEmpty(dto.getReceiveDate())) {
                throw new BaseException("取证时间不能为空！");
            }

            if (ObjectUtil.isEmpty(dto.getValidPeriod())) {
                throw new BaseException("有效期不能为空!");
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
                log.error("健康证新增异常!", e);
            }

            if (res > 0) {
                return res;
            } else {
                throw new BaseException("新增失败!");
            }

        }

        throw new BaseException("新增失败!");
    }

    @Override
    public int removeHealthCertificate(Long id) {

        int res = 0;

        try {
            res = baseMapper.deleteById(id);
        } catch (Exception e) {
            log.error("健康证删除异常!", e);
        }

        if (res > 0) {
            return res;
        } else {
            throw new BaseException("删除失败!");
        }
    }
}
