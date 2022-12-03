package com.blue.nongxin.rest;



import com.blue.nongxin.service.BankOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Map;


@Api(tags = "农信银行接口")
@RestController
@Slf4j
public class BankOrderController {

    @Resource
    private BankOrderService bankOrderService;

    //产生银行支付订单
    @PostMapping("/nongxin/makeorder.ws")
    public Map<String,Object> makeOrder(String mchtNo,String orderAmt, String openId, String orderType,String mchId,String mchName,String bankCode, String bankName) {
        return bankOrderService.makeOrder(mchtNo,orderAmt,openId,orderType,mchId,mchName,bankCode,bankName);
    }

    //查询银行支付订单
    @PostMapping("/nongxin/getorder.ws")
    public Map<String,Object> getOrder(String mchtNo,String origReqSsn,String reqSsn,String origReqTime) {
        return bankOrderService.getOrder(mchtNo,origReqSsn,reqSsn,origReqTime);
    }

    //银行下单回调
    @PostMapping("/nongxin/ordercallback.ws")
    //@RequestBody CallbackParam callbackParam
    public void orderCallBack(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            char[] buf = new char[512];
            int len = 0;
            StringBuilder contentBuffer = new StringBuilder();
            while ((len = reader.read(buf)) != -1) {
                contentBuffer.append(buf, 0, len);
            }
            bankOrderService.orderCallBack(contentBuffer.toString());
        } catch (Exception e){
            log.error("农信银行回调数据参数错误！",e);
        }

    }

}
