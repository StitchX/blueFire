package com.fire.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.entity.Bank;
import com.fire.admin.mapper.BankMapper;
import com.fire.admin.service.BankService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/25 18:23]
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements BankService {

    @Override
    public List<Bank> bankOptions() {
        return this.list();
    }

    @Override
    public List<Bank> listBankByIds(List<Integer> bankIds){
        return this.listByIds(bankIds);
    }
}
