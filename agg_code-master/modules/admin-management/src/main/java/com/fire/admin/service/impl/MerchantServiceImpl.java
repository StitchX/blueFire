package com.fire.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.dto.NoticemMerchantDto;
import com.fire.admin.dto.PageDTO;
import com.fire.admin.entity.Bank;
import com.fire.admin.entity.BusinessLicense;
import com.fire.admin.entity.Merchant;
import com.fire.admin.mapper.MerchantMapper;
import com.fire.admin.query.MerchantQuery;
import com.fire.admin.service.BankService;
import com.fire.admin.service.MerchantService;
import com.fire.admin.service.SupervisionBureauService;
import com.fire.admin.util.CommonUtil;
import com.fire.admin.util.ExeclUtil;
import com.fire.admin.vo.MerchantExcelVo;
import com.fire.utils.date.DateUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Cohen
 * @version 0.0.1
 * @description MerchantServiceImpl
 * @since 2022/1/24 11:30
 */
@Service
@Slf4j
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    @Resource
    private SupervisionBureauService supervisionBureauService;

    @Resource
    private BankService bankService;

    @Autowired
    private BusinessLicenseServiceImpl businessLicenseService;

    @Override
    public Page<Merchant> pageMerchant(MerchantQuery query) {

        log.info("pageMerchant params:{}", query);
        List<Long> orMerchantIds = null;
        if(StringUtils.isNotEmpty(query.getOperatorName())){
            //????????????
            LambdaQueryWrapper wrapper = new LambdaQueryWrapper<BusinessLicense>()
                    .like(BusinessLicense::getRepresentName, query.getOperatorName());
            List<BusinessLicense> result = businessLicenseService.list(wrapper);
            orMerchantIds = result.stream().map(BusinessLicense::getMerchantId).distinct().collect(Collectors.toList());
        }

        LambdaQueryWrapper wrapper = new LambdaQueryWrapper<Merchant>()
                .ge(query.getStartTime() != null, Merchant::getCreateTime, query.getStartTime())
                .le(query.getEndTime() != null, Merchant::getCreateTime, query.getEndTime())
                .eq(query.getBankType() != null, Merchant::getBankType, query.getBankType())
                .eq(query.getSupervisionId() != null, Merchant::getSupervisionId, query.getSupervisionId())
                .eq(query.getIndustryType() != null, Merchant::getIndustryType, query.getIndustryType())
                .in(CollectionUtil.isNotEmpty(query.getSupervisionIds()), Merchant::getSupervisionId, query.getSupervisionIds())
                .like(StringUtils.isNotBlank(query.getOperatorPhone()), Merchant::getOperatorPhone, query.getOperatorPhone())
                .like(StringUtils.isNotBlank(query.getStoreName()), Merchant::getStoreName, query.getStoreName())
                .like(StringUtils.isNotBlank(query.getAddress()), Merchant::getAddress, query.getAddress())
                .like(StringUtils.isNotBlank(query.getOperatorName()), Merchant::getOperatorName, query.getOperatorName())
                .or().in(CollectionUtil.isNotEmpty(orMerchantIds), Merchant::getMerchantId, orMerchantIds)
                .orderByDesc(Merchant::getCreateTime);


        Page page = new Page(query.getPage(), query.getSize());
        Page<Merchant> pagerResult = this.page(page, wrapper);

        List<Merchant> merchantList = pagerResult.getRecords();
        if (CollectionUtils.isEmpty(merchantList)) {
            return pagerResult;
        }

        List<Integer> bankIds = merchantList.stream().map(Merchant::getBankType).collect(Collectors.toList());
        List<Bank> banks = bankService.listBankByIds(bankIds);
        Map<Integer, String> bankMaps = banks.stream().collect(Collectors.toMap(Bank::getBankCode, Bank::getBankName));

        //??????????????????
        List<Long> merchantIds = merchantList.stream().map(Merchant::getMerchantId).collect(Collectors.toList());
        LambdaQueryWrapper businessLicenseWrapper = new LambdaQueryWrapper<BusinessLicense>()
                .in(CollectionUtil.isNotEmpty(merchantIds), BusinessLicense::getMerchantId, merchantIds);
        List<BusinessLicense> businessLicenseList = businessLicenseService.list(businessLicenseWrapper);
        Map<Long, String> businessLicenseMaps = businessLicenseList.stream().collect(Collectors.toMap(BusinessLicense::getMerchantId, BusinessLicense::getRepresentName, (key1, key2) -> key1));

        merchantList.forEach(item -> {
            item.setOperatorPhone(CommonUtil.handlePhoneNumber(item.getOperatorPhone()));
            item.setBankName(bankMaps.get(item.getBankType()));
            item.setRepresentName(businessLicenseMaps.get(item.getMerchantId()));
        });

        return pagerResult;
    }

    /**
     * @param noticemMerchantDto: ????????????????????????
     * @description: ???????????????????????????
     * @return: java.util.Set<java.lang.Long>
     * @author: liuliu
     * @date: 2022-02-23 16:41
     */
    @Override
    public IPage<Merchant> queryMerchantBySupervisionIds(NoticemMerchantDto noticemMerchantDto) {
        Page<Merchant> page = new Page<>();
        PageDTO pageDTO = noticemMerchantDto.getPageDTO();
        if (ObjectUtil.isNotEmpty(pageDTO) && ObjectUtil.isNotEmpty(pageDTO.getCurrent()) && ObjectUtil.isNotEmpty(pageDTO.getSize())) {
            page.setSize(pageDTO.getSize());
            page.setCurrent(pageDTO.getCurrent());
        }
        return baseMapper.selectMerchantByNotice(page, noticemMerchantDto);
    }

    /**
     * ??????????????????
     *
     * @param query
     * @param response
     */
    @Override
    public int merchantInfoExport(MerchantQuery query, HttpServletResponse response) {
        List<Long> recordIds = query.getRecordIds();

        query.setPage(1);
        query.setSize(-1);

        Page<Merchant> merchantDTOPage = this.pageMerchant(query);
        List<Merchant> records = merchantDTOPage.getRecords();

        List<MerchantExcelVo> list = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(records)) {
            if (CollectionUtils.isEmpty(recordIds)) {
                //?????????
                records.forEach(dto -> {
                    MerchantExcelVo vo = MerchantExcelVo.builder()
                            .merchantId(dto.getMerchantId())
                            .storeName(ObjectUtil.isEmpty(dto.getStoreName()) ? "" : dto.getStoreName())
                            .operatorName(ObjectUtil.isEmpty(dto.getOperatorName()) ? "" : dto.getOperatorName())
                            .operatorPhone(ObjectUtil.isEmpty(dto.getOperatorPhone()) ? "" : DesensitizedUtil.mobilePhone(dto.getOperatorPhone()))
                            .supervisionName(ObjectUtil.isEmpty(dto.getSupervisionId()) ? "" : supervisionBureauService.getSupervisionBureauById(dto.getSupervisionId()).getSupervisionName())
                            .businessPlace(ObjectUtil.isEmpty(dto.getBusinessPlace()) ? "" : dto.getBusinessPlace())
                            .industryType(ObjectUtil.isEmpty(dto.getIndustryType()) ? "" : switch (dto.getIndustryType()) {
                                case 1 -> "??????";
                                case 2 -> "??????";
                                default -> "";
                            })
                            .bankType(ObjectUtil.isEmpty(dto.getBankType()) ? "" : switch (dto.getBankType()) {
                                case 105 -> "????????????";
                                case 103 -> "????????????";
                                case 441 -> "????????????";
                                case 496 -> "??????????????????";
                                default -> "";
                            })
                            .createTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dto.getCreateTime()))
                            .build();
                    list.add(vo);
                });
            } else {
                //??????
                recordIds.forEach(id -> {
                    records.forEach(dto -> {
                        if (dto.getMerchantId().equals(id)) {
                            MerchantExcelVo vo = MerchantExcelVo.builder()
                                    .merchantId(dto.getMerchantId())
                                    .storeName(ObjectUtil.isEmpty(dto.getStoreName()) ? "" : dto.getStoreName())
                                    .operatorName(ObjectUtil.isEmpty(dto.getOperatorName()) ? "" : dto.getOperatorName())
                                    .operatorPhone(ObjectUtil.isEmpty(dto.getOperatorPhone()) ? "" : DesensitizedUtil.mobilePhone(dto.getOperatorPhone()))
                                    .supervisionName(ObjectUtil.isEmpty(dto.getSupervisionId()) ? "" : supervisionBureauService.getSupervisionBureauById(dto.getSupervisionId()).getSupervisionName())
                                    .businessPlace(ObjectUtil.isEmpty(dto.getBusinessPlace()) ? "" : dto.getBusinessPlace())
                                    .industryType(ObjectUtil.isEmpty(dto.getIndustryType()) ? "" : switch (dto.getIndustryType()) {
                                        case 1 -> "??????";
                                        case 2 -> "??????";
                                        default -> "";
                                    })
                                    .bankType(ObjectUtil.isEmpty(dto.getBankType()) ? "" : switch (dto.getBankType()) {
                                        case 105 -> "????????????";
                                        case 103 -> "????????????";
                                        case 441 -> "????????????";
                                        case 496 -> "??????????????????";
                                        default -> "";
                                    })
                                    .createTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(dto.getCreateTime()))
                                    .build();
                            list.add(vo);
                        }
                    });
                });
            }
        }

        String fileName = DateUtils.formatDateStr(LocalDate.now()).concat("_").concat("????????????");
        ServletOutputStream stream = null;
        ExcelWriter excelWriter = null;

        try {
            stream = response.getOutputStream();
            excelWriter = ExeclUtil.ExeclOut(fileName, response);
            excelWriter.setOnlyAlias(true);

            Workbook workbook = excelWriter.getWorkbook();
            workbook.setSheetName(0, "????????????");

            excelWriter.setColumnWidth(0, 20);
            excelWriter.setColumnWidth(1, 20);
            excelWriter.setColumnWidth(2, 15);
            excelWriter.setColumnWidth(3, 20);
            excelWriter.setColumnWidth(4, 20);
            excelWriter.setColumnWidth(5, 20);
            excelWriter.setColumnWidth(6, 20);
            excelWriter.setColumnWidth(7, 20);
            excelWriter.addHeaderAlias("storeName", "????????????");
            excelWriter.addHeaderAlias("operatorName", "????????????");
            excelWriter.addHeaderAlias("operatorPhone", "????????????");
            excelWriter.addHeaderAlias("supervisionName", "?????????");
            excelWriter.addHeaderAlias("marketName", "????????????");
            excelWriter.addHeaderAlias("industryType", "????????????");
            excelWriter.addHeaderAlias("bankType", "????????????");
            excelWriter.addHeaderAlias("createTime", "????????????");
            excelWriter.write(list);
        } catch (UnsupportedEncodingException e) {
            log.error("???????????????????????????", e);
        } catch (IOException e) {
            log.error("???????????????????????????", e);
        } finally {
            excelWriter.flush(stream, true);
            try {
                stream.flush();
            } catch (IOException e) {
                log.error("???????????????????????????", e);
            }
            excelWriter.close();
        }
        return list.size();
    }
}
