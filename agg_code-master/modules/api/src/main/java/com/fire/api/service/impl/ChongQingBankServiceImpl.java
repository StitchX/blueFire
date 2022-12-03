package com.fire.api.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.api.mapper.ChongQingBankMapper;
import com.fire.api.request.ChongQingBankParam;
import com.fire.api.service.ChongQingBankService;
import com.fire.api.vo.ChongQingBankVo;
import com.fire.common.exception.BaseException;
import com.fire.dto.bank.ChongQingBank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author: admin
 * @Description:
 * @date: 2022-01-04 15:10
 */
@Slf4j
@Service
public class ChongQingBankServiceImpl extends ServiceImpl<ChongQingBankMapper, ChongQingBank> implements ChongQingBankService {

    /**
     * 密钥上传
     *
     * @param param 重庆银行请求数据传输对象
     */
    @Override
    public void saveSecretKey(ChongQingBankParam param) {

        if (ObjectUtil.isNotEmpty(param)) {
            if (ObjectUtil.isEmpty(param.getBusinessId())) {
                throw new BaseException("商户ID不能为空！");
            }

            if (ObjectUtil.isEmpty(param.getCodeId())) {
                throw new BaseException("放心码商户id不能为空！");
            }

            if (ObjectUtil.isEmpty(param.getChongQingSecret())) {
                throw new BaseException("商户密钥不能为空！");
            }

            if (!NumberUtil.isLong(param.getCodeId().toString())) {
                throw new BaseException("放心码商户ID格式错误，只能是正整数！");
            }

            ChongQingBank chongQingBank = ChongQingBank.builder()
                    .mchId(param.getCodeId())
                    .chongQingMchId(param.getBusinessId())
                    .chongQingSecret(param.getChongQingSecret())
                    .createTime(String.valueOf(new Date().getTime() / 1000))
                    .creator("李鸿兰")
                    .updateTime(String.valueOf(new Date().getTime() / 1000))
                    .updator("李鸿兰")
                    .build();

            if (ObjectUtil.isEmpty(this.getBusinessName(param.getCodeId()))) {
                throw new BaseException("该商户不存在，上传失败！");
            }

            if (ObjectUtil.isNotEmpty(baseMapper.getExistBusinessId(param.getBusinessId()))) {
                throw new BaseException("商户id已存在，不能重复添加！");
            }

            if (ObjectUtil.isNotEmpty(baseMapper.getExistCodeId(param.getCodeId()))) {
                throw new BaseException("放心码id已存在，不能重复添加！");
            }

            int res = baseMapper.saveSecretKey(chongQingBank);

            if (res == 0) {
                throw new BaseException("密钥上传失败！");
            }
        }

    }

    /**
     * 密钥列表带分页
     *
     * @param param 重庆银行请求数据传输对象
     * @return 列表分页数据
     */
    @Override
    public IPage<ChongQingBankVo> getSecretKeyList(ChongQingBankParam param) {
        Page<ChongQingBankVo> page1 = new Page<>();

        if (ObjectUtil.isNotEmpty(param.getCurrent()) && ObjectUtil.isNotEmpty(param.getSize())) {
            page1.setCurrent(param.getCurrent());
            page1.setSize(param.getSize());
        }

        return baseMapper.querySecretKeyList(page1);
    }

    /**
     * 获取回显商户名称
     *
     * @param id 放心码商户id
     * @return 回显商户名称
     */
    @Override
    public ChongQingBankVo getBusinessName(Long id) {
        if (ObjectUtil.isEmpty(id)) {
            return new ChongQingBankVo();
        }
        return baseMapper.queryBusinessName(id);
    }

}
