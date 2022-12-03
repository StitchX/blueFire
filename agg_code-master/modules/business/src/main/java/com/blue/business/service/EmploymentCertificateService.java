package com.blue.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.business.domain.EmploymentCertificate;
import com.blue.business.dto.EmploymentCertificateDto;
import com.blue.business.vo.EmploymentCertificateVo;

import java.util.List;

/**
 * @Description:
 * @date: 2022-03-21 10:49
 */
public interface EmploymentCertificateService extends IService<EmploymentCertificate> {

    /**
     * 从业证列表
     * @return
     */
    List<EmploymentCertificateVo> employmentCertificateList(EmploymentCertificateDto dto);

    /**
     * 新增从业证
     * @param dto
     * @return
     */
    int addEmploymentCertificate(EmploymentCertificateDto dto);

    /**
     * 删除从业证
     * @param id
     * @return
     */
    int removeEmploymentCertificate(Long id);

}
