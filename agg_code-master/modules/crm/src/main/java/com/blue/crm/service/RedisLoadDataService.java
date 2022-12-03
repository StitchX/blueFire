package com.blue.crm.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blue.crm.converter.MerchantConverter;
import com.blue.crm.entity.Bank;
import com.blue.crm.entity.CategoryInfo;
import com.blue.crm.entity.FoodBusinessLicense;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.cacheDto.RedisMerchantInfo;
import com.fire.dto.constants.EntityConstants;
import com.fire.dto.entity.BankInfo;
import com.fire.dto.entity.BusinessLicense;
import com.fire.dto.entity.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fire.dto.enums.RedisKey.MERCHANT_INFO;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/10 17:11]
 */

@Service
@Slf4j
public class RedisLoadDataService {

    @Autowired
    private JedisCluster redisTemplate;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private BusinessLicenseService businessLicenseService;

    @Autowired
    private FoodBusinessLicenseService foodBusinessLicenseService;

    @Autowired
    private BankService bankService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BankInfoService bankInfoService;


    public void loadMerchantData() {
        log.info("RedisLoadDataService: 准备加载商户数据缓存至Redis...");

        List<Merchant> merchantList = merchantService.list();
        if(CollectionUtil.isEmpty(merchantList)){
            return;
        }

        List<Long> merchantIds = merchantList.stream().map(Merchant::getMerchantId).collect(Collectors.toList());
        List<Integer> bankIds = merchantList.stream().map(Merchant::getBankType).distinct().collect(Collectors.toList());
        List<Long> categoryIds = merchantList.stream().map(Merchant::getIndustryType).distinct().collect(Collectors.toList());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in(EntityConstants.MerchantConst.MERCHANT_ID, merchantIds);
        List<BusinessLicense> businessLicenseList = businessLicenseService.list(wrapper);
        Map<Long, BusinessLicense> businessLicenseMap = null;
        if (CollectionUtil.isNotEmpty(businessLicenseList)){
            businessLicenseMap = businessLicenseList.stream().collect(Collectors.toMap(BusinessLicense::getMerchantId, Function.identity(), (k1, k2) -> k2));
        }

        List<FoodBusinessLicense> foodBusinessLicenseList = foodBusinessLicenseService.list(wrapper);
        Map<Long, FoodBusinessLicense> foodBusinessLicenseMap = null;
        if(CollectionUtil.isNotEmpty(foodBusinessLicenseList)){
            foodBusinessLicenseMap = foodBusinessLicenseList.stream().collect(Collectors.toMap(FoodBusinessLicense::getMerchantId, Function.identity(), (k1, k2) -> k2));
        }

        List<BankInfo> bankInfoList = bankInfoService.list(wrapper);
        Map<Long, BankInfo> bankInfoMap = null;
        if(CollectionUtil.isNotEmpty(bankInfoList)){
            bankInfoMap = bankInfoList.stream().collect(Collectors.toMap(BankInfo::getMerchantId, Function.identity()));
        }

        QueryWrapper<Bank> bankWrapper = new QueryWrapper();
        bankWrapper.in(EntityConstants.BankConst.BANK_CODE ,bankIds);
        List<Bank> bankList = bankService.list(bankWrapper);
        Map<Integer, String> bankMap = null;
        if(CollectionUtil.isNotEmpty(bankList)){
            bankMap = bankList.stream().collect(Collectors.toMap(Bank::getBankCode, Bank::getBankName));
        }

        QueryWrapper<CategoryInfo> categoryInfoQueryWrapper = new QueryWrapper();
        categoryInfoQueryWrapper.in(EntityConstants.CategoryInfoConst.CATEGORY_ID,categoryIds);
        List<CategoryInfo> categoryInfos = categoryService.list(categoryInfoQueryWrapper);
        Map<Long, String> categoryMap = null;
        if(CollectionUtil.isNotEmpty(categoryInfos)){
            categoryMap = categoryInfos.stream().collect(Collectors.toMap(CategoryInfo::getCategoryId, CategoryInfo::getCategoryName));
        }

        List<RedisMerchantInfo> redisMerchantInfos = MerchantConverter.INSTANCE.entities2Redis(merchantList);
        Map<Long, BusinessLicense> finalBusinessLicenseMap = businessLicenseMap;
        Map<Long, FoodBusinessLicense> finalFoodBusinessLicenseMap = foodBusinessLicenseMap;
        Map<Integer, String> finalBankMap = bankMap;
        Map<Long, String> finalCategoryMap = categoryMap;
        Map<Long, BankInfo> finalBankInfoMap = bankInfoMap;
        redisMerchantInfos.forEach(item -> {
            if(ObjectUtil.isNotNull(finalBusinessLicenseMap) && ObjectUtil.isNotNull(finalBusinessLicenseMap.get(item.getMerchantId()))) {
                item.setBusinessUrl(finalBusinessLicenseMap.get(item.getMerchantId()).getImagesUrl());
                item.setSocialCreditCode(finalBusinessLicenseMap.get(item.getMerchantId()).getSocialCreditCode());
                item.setRepresentName(finalBusinessLicenseMap.get(item.getMerchantId()).getRepresentName());
            }

            if(ObjectUtil.isNotNull(finalFoodBusinessLicenseMap) && ObjectUtil.isNotNull(finalFoodBusinessLicenseMap.get(item.getMerchantId()))){
                item.setFoodLicenseUrl(finalFoodBusinessLicenseMap.get(item.getMerchantId()).getImagesUrl());
            }
            if(ObjectUtil.isNotNull(finalBankInfoMap) && ObjectUtil.isNotNull(finalBankInfoMap.get(item.getMerchantId()))) {
                log.info("构建银行相关信息:{}", finalBankInfoMap.get(item.getMerchantId()));
                item.setBankMerchantNum(finalBankInfoMap.get(item.getMerchantId()).getCustId());
                item.setSecretKey(finalBankInfoMap.get(item.getMerchantId()).getSecretKey());
            }

            item.setBankName(finalBankMap.get(item.getBankId()));
            item.setIndustryClassification(finalCategoryMap.get(item.getIndustryClassificationId()));

            log.info("RedisLoadDataService: 缓存数据： {}", item);
            ObjectMapper om = new ObjectMapper();
            try {
                redisTemplate.hset(MERCHANT_INFO.key() + item.getMerchantId() / 10000, item.getMerchantId().toString(), om.writeValueAsString(item));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        log.info("RedisLoadDataService: 加载商户数据缓存至Redis已完成...");
    }
}
