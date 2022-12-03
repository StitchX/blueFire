package com.blue.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.converter.MerchantConverter;
import com.blue.business.mapper.MerchantMapper;
import com.blue.business.security.util.PreSecurityUser;
import com.blue.business.service.*;
import com.blue.business.vo.AddressInfoVo;
import com.blue.business.vo.CategoryInfoVo;
import com.blue.business.vo.IndexMerchantVo;
import com.blue.business.vo.MerchantDetailVo;
import com.fire.dto.constants.EntityConstants;
import com.fire.dto.entity.*;
import com.fire.dto.enums.MerchantEnums;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/23 15:08]
 */
@Service
@Slf4j
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    @Autowired
    private AddressInfoService addressInfoService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BankAppointmentService bankAppointmentService;

    @Autowired
    private BusinessLicenseService businessLicenseService;

    @Autowired
    private FoodBusinessLicenseService foodBusinessLicenseService;

    @Autowired
    private BankService bankService;

    @Override
    public BaseRestResponse<IndexMerchantVo> indexInfo() {
        PreSecurityUser loginUser = (PreSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("MerchantServiceImpl:indexInfo: 当前登陆用户: {}", loginUser);
        if(ObjectUtil.isNull(loginUser)){
            log.error("MerchantServiceImpl:indexInfo: 获取当前登陆用户失败");
        }
        Long merchantId = loginUser.getMerchantId();
        LambdaQueryWrapper<Merchant> merchantQueryWrapper = new LambdaQueryWrapper<Merchant>()
                .select(Merchant::getStoreName, Merchant::getPayCodeUrl, Merchant::getFoodSafetyStatus, Merchant::getPayStatus)
                .eq(Merchant::getMerchantId, merchantId);

        Merchant merchant = this.getOne(merchantQueryWrapper);
        IndexMerchantVo indexMerchantVo = MerchantConverter.INSTANCE.entity2IndexMerchantVo(merchant);

        return new BaseRestResponse<>(indexMerchantVo);
    }

    public BaseRestResponse merchantDetail() {
        PreSecurityUser loginUser = (PreSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("MerchantServiceImpl:indexInfo: 当前登陆用户: {}", loginUser);
        Long merchantId = loginUser.getMerchantId();
        Merchant merchant = this.getById(merchantId);
        if(merchant == null){
            return new BaseRestResponse();
        }
        MerchantDetailVo merchantDetailVo = MerchantConverter.INSTANCE.entity2Vo(merchant);
        //构建省市区信息
        if(StringUtils.isNotBlank(merchantDetailVo.getAddressCode())){
            Map<Integer, AddressInfoVo> result  = new HashMap<>();
            Map<Integer, AddressInfoVo> addressInfoMap = addressInfoService.mapAddressInfoByCode(merchantDetailVo.getAddressCode(), result);
            merchantDetailVo.setAddressInfoMap(addressInfoMap);
        }
        //构建行业类型信息
        if(merchantDetailVo.getIndustryType() != null){
            List<CategoryInfoVo> list = new ArrayList<>();
            Map<Integer, CategoryInfoVo> result  = new HashMap<>();
            Long categoryId = Long.valueOf(merchantDetailVo.getIndustryType());
            while (categoryId != 0 && categoryId != 1){
                CategoryInfoVo item = categoryService.categoryInfoById(categoryId);
                categoryId = item != null ? item.getParentCategoryId() : 0;
                list.add(item);
            }
            Collections.reverse(list);
            list.forEach(item -> {
                result.put(list.indexOf(item) + 1, item);
            });
            merchantDetailVo.setCategoryInfoMap(result);
        }


        QueryWrapper businessWrapper = Wrappers.query().eq(merchantId != null, EntityConstants.MerchantConst.MERCHANT_ID, merchantId).orderByDesc(EntityConstants.CREATE_TIME).last("limit 1");
        BusinessLicense businessLicense = businessLicenseService.getOne(businessWrapper);
        if(ObjectUtil.isNotNull(businessLicense)){
            LocalDate date = businessLicense.getValidPeriod();
            if(ObjectUtil.isNull(date) || date.getYear() == 2999){
                businessLicense.setValidPermanent(true);
            }
        }

        QueryWrapper wrapper = Wrappers.query().eq(merchantId != null, EntityConstants.MerchantConst.MERCHANT_ID, merchantId);
        BankAppointment bankAppointment = bankAppointmentService.getOne(wrapper);
        if(ObjectUtil.isNotEmpty(bankAppointment)){
            Bank bank = bankService.getById(bankAppointment.getBankId());
            merchantDetailVo.setBankName(bank.getBankName());
        }else {
            bankAppointment = new BankAppointment();
        }
        FoodBusinessLicense foodBusinessLicense = foodBusinessLicenseService.getOne(businessWrapper);
        if(ObjectUtil.isEmpty(foodBusinessLicense)){
            foodBusinessLicense = new FoodBusinessLicense();
        }

        merchantDetailVo.setAppointmentBank(bankAppointment);
        merchantDetailVo.setBusinessLicense(businessLicense);
        merchantDetailVo.setFoodBusinessLicense(foodBusinessLicense);

        return new BaseRestResponse(merchantDetailVo);
    }

    @Transactional
    public BaseRestResponse<MerchantDetailVo> editMerchant(MerchantDetailVo merchantDetailVo) {
        log.info("MerchantService editMerchant param is  {}", merchantDetailVo);
        if(null == merchantDetailVo.getMerchantId()){
            return BaseRestResponse.error("商户请求参数有误，请检查");
        }

        //判断手机号是否重复
        LambdaQueryWrapper<Merchant> merchantWrapper = new LambdaQueryWrapper<Merchant>().eq(Merchant::getMerchantId, merchantDetailVo.getMerchantId());
        Merchant merchant = this.getOne(merchantWrapper);
        String phone = merchantDetailVo.getOperatorPhone();
        if(!merchant.getOperatorPhone().equals(phone)){
            LambdaQueryWrapper<Merchant> queryWrapper = new LambdaQueryWrapper<Merchant>().eq(StringUtils.isNotBlank(phone), Merchant::getOperatorPhone, phone);
            if(this.count(queryWrapper) > 0){
                return BaseRestResponse.error("手机号重复，请重新输入。");
            }
        }

        if(null == merchantDetailVo.getBusinessLicense().getBusinessLicenseId()){
            return BaseRestResponse.error("营业执照请求参数有误，请检查");
        }

        BankAppointment bankAppointment = merchantDetailVo.getAppointmentBank();
        BusinessLicense businessLicense = merchantDetailVo.getBusinessLicense();
        FoodBusinessLicense foodBusinessLicense = merchantDetailVo.getFoodBusinessLicense();

        if(BeanUtil.isNotEmpty(bankAppointment) && bankAppointment.getBankId() != null) {
            //更新银行预约表信息
            merchantDetailVo.setStatus(MerchantEnums.Status.APPOINTMENT.getCode());
            bankAppointment.setMerchantId(merchantDetailVo.getMerchantId());
            boolean bankUpdateResult = bankAppointmentService.saveOrUpdate(bankAppointment);
            if(!bankUpdateResult){
                log.info("MerchantService:editMerchant:AppointBank表没有修改内容");
            }
        }else {
            //删除银行预约表信息
            LambdaQueryWrapper<BankAppointment> wrapper = new LambdaQueryWrapper<BankAppointment>().eq(BankAppointment::getMerchantId, merchantDetailVo.getMerchantId());
            BankAppointment db = bankAppointmentService.getOne(wrapper);
            if(ObjectUtil.isNotNull(db)){
                bankAppointmentService.remove(wrapper);
            }
            merchantDetailVo.setStatus(MerchantEnums.Status.SETTLED.getCode());
        }

        businessLicense.setMerchantId(merchantDetailVo.getMerchantId());
        LambdaUpdateWrapper<BusinessLicense> businessLicenseLambdaUpdateWrapper = new LambdaUpdateWrapper<BusinessLicense>()
                .eq(businessLicense.getBusinessLicenseId() != null, BusinessLicense::getBusinessLicenseId, businessLicense.getBusinessLicenseId());
        boolean businessLicenseUpdateResult = businessLicenseService.update(businessLicense, businessLicenseLambdaUpdateWrapper);
        if(!businessLicenseUpdateResult){
            log.info("MerchantService:editMerchant:businessLicense表没有修改内容");
        }


        if(ObjectUtil.isNotEmpty(foodBusinessLicense)){
            foodBusinessLicense.setMerchantId(merchantDetailVo.getMerchantId());
            LambdaUpdateWrapper<FoodBusinessLicense> foodBusinessLicenseLambdaUpdateWrapper = new LambdaUpdateWrapper<FoodBusinessLicense>()
                    .eq(foodBusinessLicense.getFoodBusinessLicenseId() != null, FoodBusinessLicense::getFoodBusinessLicenseId, foodBusinessLicense.getFoodBusinessLicenseId());
            boolean foodBusinessLicenseUpdateResult = foodBusinessLicenseService.update(foodBusinessLicense, foodBusinessLicenseLambdaUpdateWrapper);
            if(!foodBusinessLicenseUpdateResult){
                log.info("MerchantService:editMerchant:foodBusinessLicense表没有修改内容");
            }
        }

        Merchant updateMerchant = MerchantConverter.INSTANCE.merchantDetailVo2Merchant(merchantDetailVo);
        boolean merchantUpdateResult = this.updateById(updateMerchant);
        if(!merchantUpdateResult){
            log.info("MerchantService:editMerchant:merchant表没有修改内容");
        }

/*        //redis缓存
        RedisMerchantInfo redisMerchantInfo = buildRedisMerchantData(merchantVo);
        log.info("MerchantService: 缓存商户数据： {}", redisMerchantInfo);
        ObjectMapper om = new ObjectMapper();
        try {
            redisTemplate.hset(MERCHANT_INFO.key() + redisMerchantInfo.getMerchantId() / 10000, redisMerchantInfo.getMerchantId().toString(), om.writeValueAsString(redisMerchantInfo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/

        return new BaseRestResponse(merchantDetailVo);
    }
}
