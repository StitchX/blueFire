package com.blue.nongxin.service;

import java.util.Map;

public interface BankOrderService {

    Map<String,Object> makeOrder(String mchtNo,String orderAmt, String openId, String orderType,String mchId,String mchName,String bankCode, String bankName);

    Map<String,Object> getOrder(String mchtNo,String origReqSsn,String reqSsn,String origReqTime);

    void orderCallBack(String data);
}
