package com.blue.business.service;

import com.blue.business.dto.TransactionDetailsDto;

import java.util.Map;

/**
 * @Description:
 * @date: 2022-03-22 14:22
 */
public interface TransactionDetailsService {

    /**
     * 按日期查询交易明细
     *
     * @param dto
     * @return
     */
    Map<String, Object> getTransactionDetails(TransactionDetailsDto dto);

}
