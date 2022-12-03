package com.fire.admin.service;

import com.fire.admin.entity.Bank;

import java.util.List;

public interface BankService {
    List<Bank> bankOptions();

    List<Bank> listBankByIds(List<Integer> ids);
}
