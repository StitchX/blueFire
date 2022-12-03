package com.blue.gongshang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface BankOrderService {

    Map<String,Object> makeOrder(String mchtNo, String orderAmt, String openId, String orderType, String mchId);

    Map<String,Object> getOrder(String mchtNo,String origReqSsn);

    void callBack(HttpServletRequest req, HttpServletResponse resp);
}
