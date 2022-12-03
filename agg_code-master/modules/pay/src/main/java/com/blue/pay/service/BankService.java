package com.blue.pay.service;

import com.blue.pay.request.MakeBankParam;
import com.blue.pay.response.MakeBankResp;



/**
 * @author: admin
 * @Description:
 * @date: 2022-01-04 15:06
 */
public interface BankService  {

    /**
     * 银行下单
     * @param param 下单参数
     */
    MakeBankResp makeOrder(MakeBankParam param);



}
