package com.blue.crm.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.crm.component.ErCodeProducer;
import com.blue.crm.converter.MerchantConverter;
import com.blue.crm.dto.LoginUser;
import com.blue.crm.entity.*;
import com.blue.crm.enums.ErrorEnum;
import com.blue.crm.exception.ServiceException;
import com.blue.crm.entity.Bank;
import com.blue.crm.entity.BankAppointment;
import com.blue.crm.entity.FoodBusinessLicense;
import com.blue.crm.mapper.MerchantMapper;
import com.blue.crm.query.MerchantQuery;
import com.blue.crm.util.SecurityUtil;
import com.blue.crm.util.StringUtils;
import com.blue.crm.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.dto.cacheDto.RedisMerchantInfo;
import com.fire.dto.constants.EntityConstants;
import com.fire.dto.entity.BankInfo;
import com.fire.dto.entity.BusinessLicense;
import com.fire.dto.entity.Merchant;
import com.fire.dto.entity.MerchantUser;
import com.fire.dto.enums.MerchantEnums;
import com.fire.dto.enums.RedisKey;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.fire.dto.enums.RedisKey.MERCHANT_INFO;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/22 14:45]
 */
@Service
@Slf4j
public class MerchantService extends ServiceImpl<MerchantMapper, Merchant> {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private BankAppointmentService bankAppointmentService;

    @Autowired
    private BusinessLicenseService businessLicenseService;

    @Autowired
    private FoodBusinessLicenseService foodBusinessLicenseService;

    @Autowired
    private BDService bdService;

    @Autowired
    private AddressInfoService addressInfoService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BankService bankService;

    @Autowired
    private JedisCluster redisTemplate;

    @Autowired
    private ErCodeProducer erCodeProducer;

    @Autowired
    private MerchantUserService merchantUserService;

    @Autowired
    private EmptyCodeService emptyCodeService;

    public BaseRestResponse listMerchant(MerchantQuery query) {
        log.info("MerchantService listMerchant param is  {}", query);
        //??????????????????????????????ID
        LoginUser user = SecurityUtil.getUser();
        if(ObjectUtil.isEmpty(user)){
            return new BaseRestResponse();
        }

        if (CollectionUtil.isNotEmpty(query.getBankType())){
            LambdaQueryWrapper<BankAppointment> wrapper = new LambdaQueryWrapper<BankAppointment>().in(BankAppointment::getBankId, query.getBankType());
            List<BankAppointment> bankAppointments = bankAppointmentService.list(wrapper);
            List<Long> ids = bankAppointments.stream().map(BankAppointment::getMerchantId).collect(Collectors.toList());
            query.setIds(ids);
        }

        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<Merchant>()
                .eq(user.getBdId() != null, Merchant::getBdId, user.getBdId())
                .in(CollectionUtil.isNotEmpty(query.getIds()), Merchant::getMerchantId, query.getIds())
                .in(CollectionUtil.isNotEmpty(query.getStatus()), Merchant::getStatus, query.getStatus())
                .ge(query.getStartTime() != null, Merchant::getCreateTime, query.getStartTime())
                .le(query.getEndTime() != null, Merchant::getCreateTime, query.getEndTime())
                .and(StringUtils.isNotBlank(query.getKeywords()), i -> i.like(Merchant::getStoreName, query.getKeywords()).or().like(Merchant::getOperatorName, query.getKeywords()))
                .orderByDesc(Merchant::getCreateTime);
        Page merchantList = this.page(new Page<>(query.getPage(), query.getSize()), wrapper);
        List<Merchant> records = merchantList.getRecords();
        List<MerchantListVo> merchantListVos = MerchantConverter.INSTANCE.entity2ListVo(records);
        merchantList.setRecords(merchantListVos);
        return new BaseRestResponse(merchantList);
    }

    public BaseRestResponse merchantDetail(MerchantQuery query) {
        log.info("MerchantService merchantDetail param is  {}", query);
        Long merchantId = query.getId();
        Merchant merchant = merchantService.getById(merchantId);
        if(merchant == null){
            return new BaseRestResponse();
        }
        MerchantVo merchantVo = MerchantConverter.INSTANCE.entity2Vo(merchant);
        //?????????????????????
        if(StringUtils.isNotBlank(merchantVo.getAddressCode())){
            Map<Integer, AddressInfoVo> result  = new HashMap<>();
            Map<Integer, AddressInfoVo> addressInfoMap = addressInfoService.mapAddressInfoByCode(merchantVo.getAddressCode(), result);
            merchantVo.setAddressInfoMap(addressInfoMap);
        }
        //????????????????????????
        if(merchantVo.getIndustryType() != null){
            List<CategoryInfoVo> list = new ArrayList<>();
            Map<Integer, CategoryInfoVo> result  = new HashMap<>();
            Long categoryId = Long.valueOf(merchantVo.getIndustryType());
            //0????????????????????????????????????1???????????????????????????
            while (categoryId != 0 && categoryId != 1){
                CategoryInfoVo item = categoryService.categoryInfoById(categoryId);
                categoryId = item != null ? item.getParentCategoryId() : 0;
                list.add(item);
            }
            Collections.reverse(list);
            list.forEach(item -> {
                result.put(list.indexOf(item) + 1, item);
            });
            merchantVo.setCategoryInfoMap(result);
        }

        QueryWrapper wrapper = Wrappers.query().eq(merchantId != null, EntityConstants.MerchantConst.MERCHANT_ID, merchantId);
        BankAppointment bankAppointment = bankAppointmentService.getOne(wrapper);
        QueryWrapper businessWrapper = Wrappers.query().eq(merchantId != null, EntityConstants.MerchantConst.MERCHANT_ID, merchantId).orderByDesc(EntityConstants.CREATE_TIME).last("limit 1");
        BusinessLicense businessLicense = businessLicenseService.getOne(businessWrapper);
        if(ObjectUtil.isNotNull(businessLicense)){
            LocalDate date = businessLicense.getValidPeriod();
            if(ObjectUtil.isNull(date) || date.getYear() == 2999){
                businessLicense.setValidPermanent(true);
            }
        }
        FoodBusinessLicense foodBusinessLicense = foodBusinessLicenseService.getOne(businessWrapper);
        if(ObjectUtil.isNotEmpty(bankAppointment)){
            Bank bank = bankService.getById(bankAppointment.getBankId());
            merchantVo.setBankName(bank.getBankName());
        }

        merchantVo.setAppointmentBank(bankAppointment);
        merchantVo.setBusinessLicense(businessLicense);
        merchantVo.setFoodBusinessLicense(foodBusinessLicense);

        return new BaseRestResponse(merchantVo);
    }

    @Transactional
    public BaseRestResponse<MerchantVo> addMerchant(MerchantVo merchantVo){
        log.info("MerchantService addMerchant param is  {}", merchantVo);
        //???????????????????????????
        String phone = merchantVo.getOperatorPhone();
        LambdaQueryWrapper<Merchant> queryWrapper = new LambdaQueryWrapper<Merchant>()
                .eq(StringUtils.isNotEmpty(phone), Merchant::getOperatorPhone, phone);
        if(this.count(queryWrapper) > 0){
            return BaseRestResponse.error("????????????????????????????????????");
        }
        //??????????????????????????????ID
        LoginUser user = SecurityUtil.getUser();
        merchantVo.setBdId(user.getBdId());
        //??????BD??????
        BD bdInfo = bdService.getLoginBDInfo();
        if(BeanUtil.isNotEmpty(bdInfo)){
            merchantVo.setCreator(bdInfo.getName());
            merchantVo.setBd(bdInfo.getName());
            merchantVo.setBdm(bdInfo.getBdmName());
        }

        BankAppointment bankAppointment = merchantVo.getAppointmentBank();
        if(ObjectUtil.isNotEmpty(bankAppointment) && bankAppointment.getBankId() != null && bankAppointment.getAppointmentTime() != null) {
            //????????????????????? ??????????????????
            merchantVo.setStatus(MerchantEnums.Status.APPOINTMENT.getCode());
        }

        Long merchantId = redisTemplate.incr(RedisKey.MERCHANT_ID_INCR.key());
        log.info("???redis???????????????id??????{}", merchantId);
        merchantVo.setMerchantId(merchantId);
        boolean merchantSaveResult = merchantService.save(merchantVo);
        if(!merchantSaveResult){
            //????????????
            throw new ServiceException(ErrorEnum.SYSTEM_ERROR.getCode(), "??????????????????");
        }

        BusinessLicense businessLicense = merchantVo.getBusinessLicense();
        FoodBusinessLicense foodBusinessLicense = merchantVo.getFoodBusinessLicense();

        //??????????????????
        if(BeanUtil.isNotEmpty(bankAppointment) && bankAppointment.getBankId() != null) {
            bankAppointment.setMerchantId(merchantVo.getMerchantId());
            merchantVo.setStatus(MerchantEnums.Status.APPOINTMENT.getCode());
            boolean bankAppointmentSaveResult = bankAppointmentService.save(bankAppointment);
            if(!bankAppointmentSaveResult){
                throw new ServiceException(ErrorEnum.SYSTEM_ERROR.getCode(), "??????????????????????????????");
            }
        }

        //??????????????????
        businessLicense.setMerchantId(merchantVo.getMerchantId());
        businessLicense.setCreator(user.getUsername());
        boolean businessLicenseSaveResult = businessLicenseService.save(businessLicense);
        if(!businessLicenseSaveResult){
            throw new ServiceException(ErrorEnum.SYSTEM_ERROR.getCode(), "????????????????????????");
        }

        //???????????????????????????
        if(ObjectUtil.isNotNull(foodBusinessLicense) && StringUtils.isNotEmpty(foodBusinessLicense.getImagesUrl())){
            foodBusinessLicense.setMerchantId(merchantVo.getMerchantId());
            foodBusinessLicense.setCreator(user.getUsername());
            boolean foodBusinessLicenseSaveResult = foodBusinessLicenseService.save(foodBusinessLicense);
            if(!foodBusinessLicenseSaveResult){
                throw new ServiceException(ErrorEnum.SYSTEM_ERROR.getCode(), "???????????????????????????");
            }
        }

        //??????????????????
        MerchantUser merchantUser = new MerchantUser();
        merchantUser.setUsername(merchantVo.getOperatorPhone());
        merchantUser.setMerchantId(merchantId);
        boolean merchantUserResult = merchantUserService.save(merchantUser);
        if(!merchantUserResult){
            throw new ServiceException(ErrorEnum.SYSTEM_ERROR.getCode(), "????????????????????????");
        }

        //redis??????
        RedisMerchantInfo redisMerchantInfo = buildRedisMerchantData(merchantVo);
        log.info("MerchantService: ????????????????????? {}", redisMerchantInfo);
        ObjectMapper om = new ObjectMapper();
        Long result = null;
        try {
            result = redisTemplate.hset(MERCHANT_INFO.key() + redisMerchantInfo.getMerchantId() / 10000, redisMerchantInfo.getMerchantId().toString(), om.writeValueAsString(redisMerchantInfo));
        } catch (JsonProcessingException e) {
            log.error("????????????json????????????", e);
        } catch (Exception e){
            log.error("????????????redis????????????", e);
        }
        if(null == result || result <= 0){
            log.error("redis????????????");
            throw new ServiceException(ErrorEnum.SYSTEM_ERROR.getCode(), "redis????????????");
        }

        //??????MQ??????
        try {
            erCodeProducer.onewaySend(merchantVo.getMerchantId(), merchantVo.getStoreName(), merchantVo.getSupervisionName());
        } catch (Exception e){
            log.error("??????ercode MQ??????", e);
        }

        return new BaseRestResponse(merchantVo);
    }

    /**
     * ??????????????????
     * @param params param.count ???????????? ??????100???
     * @return
     */
    @Transactional
    public BaseRestResponse addEmptyCodeMerchant(GenerateEmptyCodeParams params){
        Integer count = params.getCount();
        List<Merchant> merchants = new ArrayList<>();
        List<EmptyCode> emptyCodes = new ArrayList<>();
        for (int i = 0; i<count; i++) {
            Long merchantId = redisTemplate.incr(RedisKey.MERCHANT_ID_INCR.key());
            log.info("???redis???????????????id??????{}", merchantId);
            Merchant merchant = new Merchant();
            merchants.add(merchant);
            EmptyCode emptyCode = EmptyCode.builder().idType(1).merchantId(merchantId).build();
            emptyCodes.add(emptyCode);
        }
        this.saveBatch(merchants);
        emptyCodeService.saveBatch(emptyCodes);
        return new BaseRestResponse();
    }

    @Transactional
    public BaseRestResponse<MerchantVo> editMerchant(MerchantVo merchantVo) {
        log.info("MerchantService editMerchant param is  {}", merchantVo);
        if(null == merchantVo.getMerchantId()){
            return BaseRestResponse.error("????????????????????????????????????");
        }

        //???????????????????????????
        LambdaQueryWrapper<EmptyCode> emptyCodeLambdaQueryWrapper = new LambdaQueryWrapper<EmptyCode>()
                .eq(EmptyCode::getMerchantId, merchantVo.getMerchantId());
        EmptyCode emptyCode = emptyCodeService.getOne(emptyCodeLambdaQueryWrapper);
        if(ObjectUtil.isNotEmpty(emptyCode)){
            log.info("MerchantService editMerchant?????????????????????, id:{}", merchantVo.getMerchantId());
            //?????????????????????????????????
            emptyCodeService.remove(emptyCodeLambdaQueryWrapper);
        }


        //???????????????????????????
        LambdaQueryWrapper<Merchant> merchantWrapper = new LambdaQueryWrapper<Merchant>().eq(Merchant::getMerchantId, merchantVo.getMerchantId());
        Merchant merchant = merchantService.getOne(merchantWrapper);
        if(ObjectUtil.isNull(merchant)){
            log.error("???????????????, merchantId??? {}", merchantVo.getMerchantId());
            throw new ServiceException(ErrorEnum.SYSTEM_ERROR.getCode(), "???????????????");
        }
        String phone = merchantVo.getOperatorPhone();
        if(!merchant.getOperatorPhone().equals(phone)){
            LambdaQueryWrapper<Merchant> queryWrapper = new LambdaQueryWrapper<Merchant>().eq(StringUtils.isNotEmpty(phone), Merchant::getOperatorPhone, phone);
            if(this.count(queryWrapper) > 0){
                return BaseRestResponse.error("????????????????????????????????????");
            }
        }

        merchantVo.setUpdateTime(Timestamp.from(Instant.now()));
        //??????BD??????
        BD bdInfo = bdService.getLoginBDInfo();
        if(ObjectUtil.isNotEmpty(bdInfo)){
            merchantVo.setUpdater(bdInfo.getName());
        }


        if(null == merchantVo.getBusinessLicense().getBusinessLicenseId()){
            return BaseRestResponse.error("??????????????????????????????????????????");
        }

        BankAppointment bankAppointment = merchantVo.getAppointmentBank();
        BusinessLicense businessLicense = merchantVo.getBusinessLicense();
        FoodBusinessLicense foodBusinessLicense = merchantVo.getFoodBusinessLicense();

        if(BeanUtil.isNotEmpty(bankAppointment) && bankAppointment.getBankId() != null) {
            //???????????????????????????
            merchantVo.setStatus(MerchantEnums.Status.APPOINTMENT.getCode());
            bankAppointment.setMerchantId(merchantVo.getMerchantId());
            boolean bankUpdateResult = bankAppointmentService.saveOrUpdate(bankAppointment);
            if(!bankUpdateResult){
                log.info("MerchantService:editMerchant:AppointBank?????????????????????");
            }
        }else {
            //???????????????????????????
            LambdaQueryWrapper<BankAppointment> wrapper = new LambdaQueryWrapper<BankAppointment>().eq(BankAppointment::getMerchantId, merchantVo.getMerchantId());
            BankAppointment db = bankAppointmentService.getOne(wrapper);
            if(ObjectUtil.isNotNull(db)){
                bankAppointmentService.remove(wrapper);
            }
            merchantVo.setStatus(MerchantEnums.Status.SETTLED.getCode());
        }

        businessLicense.setMerchantId(merchantVo.getMerchantId());
        LambdaUpdateWrapper<BusinessLicense> businessLicenseLambdaUpdateWrapper = new LambdaUpdateWrapper<BusinessLicense>()
                .eq(businessLicense.getBusinessLicenseId() != null, BusinessLicense::getBusinessLicenseId, businessLicense.getBusinessLicenseId());
        boolean businessLicenseUpdateResult = businessLicenseService.update(businessLicense, businessLicenseLambdaUpdateWrapper);
        if(!businessLicenseUpdateResult){
            log.info("MerchantService:editMerchant:businessLicense?????????????????????");
        }


        if(ObjectUtil.isNotEmpty(foodBusinessLicense)){
            foodBusinessLicense.setMerchantId(merchantVo.getMerchantId());
            foodBusinessLicense.setUpdater(bdInfo.getName());
            LambdaUpdateWrapper<FoodBusinessLicense> foodBusinessLicenseLambdaUpdateWrapper = new LambdaUpdateWrapper<FoodBusinessLicense>()
                    .eq(foodBusinessLicense.getFoodBusinessLicenseId() != null, FoodBusinessLicense::getFoodBusinessLicenseId, foodBusinessLicense.getFoodBusinessLicenseId());
            boolean foodBusinessLicenseUpdateResult = foodBusinessLicenseService.update(foodBusinessLicense, foodBusinessLicenseLambdaUpdateWrapper);
            if(!foodBusinessLicenseUpdateResult){
                log.info("MerchantService:editMerchant:foodBusinessLicense?????????????????????");
            }
        }

        boolean merchantUpdateResult = merchantService.updateById(merchantVo);
        if(!merchantUpdateResult){
            log.info("MerchantService:editMerchant:merchant?????????????????????");
        }

        //???????????????????????????
        MerchantUser merchantUser = new MerchantUser();
        merchantUser.setUsername(merchantVo.getOperatorPhone());
        merchantUser.setMerchantId(merchantVo.getMerchantId());
        LambdaQueryWrapper<MerchantUser> userLambdaQueryWrapper = new LambdaQueryWrapper<MerchantUser>().eq(MerchantUser::getMerchantId, merchantUser.getMerchantId());
        boolean merchantUserResult = merchantUserService.saveOrUpdate(merchantUser, userLambdaQueryWrapper);
        if(!merchantUserResult){
            log.info("MerchantService:editMerchant:merchant_user???????????????");
        }

        //redis??????
        RedisMerchantInfo redisMerchantInfo = buildRedisMerchantData(merchantVo);
        log.info("MerchantService: ????????????????????? {}", redisMerchantInfo);
        ObjectMapper om = new ObjectMapper();
        Long result = null;
        try {
            result = redisTemplate.hset(MERCHANT_INFO.key() + redisMerchantInfo.getMerchantId() / 10000, redisMerchantInfo.getMerchantId().toString(), om.writeValueAsString(redisMerchantInfo));
        } catch (JsonProcessingException e) {
            log.error("????????????json????????????", e);
        } catch (Exception e){
            log.error("????????????redis????????????", e);
        }
        if(null == result || result <= 0) {
            log.error("redis????????????");
            throw new ServiceException(ErrorEnum.SYSTEM_ERROR.getCode(), "redis????????????");
        }

        return new BaseRestResponse(merchantVo);
    }

    private RedisMerchantInfo buildRedisMerchantData(MerchantVo merchantVo) {
        RedisMerchantInfo redisMerchantInfo = MerchantConverter.INSTANCE.merchantToRedisMerchantInfo(merchantVo);
        BusinessLicense businessLicense = merchantVo.getBusinessLicense();
        if(ObjectUtil.isNotNull(businessLicense)){
            redisMerchantInfo.setRepresentName(businessLicense.getRepresentName());
            redisMerchantInfo.setSocialCreditCode(businessLicense.getSocialCreditCode());
            redisMerchantInfo.setBusinessUrl(businessLicense.getImagesUrl());
        }
        FoodBusinessLicense foodBusinessLicense = merchantVo.getFoodBusinessLicense();
        if(ObjectUtil.isNotNull(foodBusinessLicense)){
            redisMerchantInfo.setFoodLicenseUrl(foodBusinessLicense.getImagesUrl());
        }

        if(merchantVo.getBankType() != null){
            QueryWrapper<Bank> bankWrapper = new QueryWrapper();
            bankWrapper.eq(EntityConstants.BankConst.BANK_CODE, merchantVo.getBankType());
            Bank bank = bankService.getOne(bankWrapper);
            if(ObjectUtil.isNotNull(bank)){
                redisMerchantInfo.setBankName(bank.getBankName());
            }
        }

        if(merchantVo.getIndustryType() != null){
            QueryWrapper<CategoryInfo> industryWrapper = new QueryWrapper();
            industryWrapper.eq(EntityConstants.CategoryInfoConst.CATEGORY_ID, merchantVo.getIndustryType());
            CategoryInfo categoryInfo = categoryService.getOne(industryWrapper);
            if(ObjectUtil.isNotNull(categoryInfo)) {
                redisMerchantInfo.setIndustryClassification(categoryInfo.getCategoryName());
            }
        }

        return  redisMerchantInfo;
    }
}
