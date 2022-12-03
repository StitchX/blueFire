package com.blue.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.business.domain.HealthCertificate;
import com.blue.business.dto.HealthCertificateDto;
import com.blue.business.vo.HealthCertificateVo;

import java.util.List;

/**
 * @Description:
 * @date: 2022-03-21 10:49
 */
public interface HealthCertificateService extends IService<HealthCertificate> {

    /**
     * 健康证列表
     * @return
     */
    List<HealthCertificateVo> healthCertificateList(HealthCertificateDto dto);

    /**
     * 新增健康证
     * @param dto
     * @return
     */
    int addHealthCertificate(HealthCertificateDto dto);

    /**
     * 删除健康证
     * @param id
     * @return
     */
    int removeHealthCertificate(Long id);
}
