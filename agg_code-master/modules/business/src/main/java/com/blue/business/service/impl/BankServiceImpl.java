package com.blue.business.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.mapper.BankMapper;
import com.blue.business.service.BankService;
import com.fire.dto.entity.Bank;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements BankService {

}
