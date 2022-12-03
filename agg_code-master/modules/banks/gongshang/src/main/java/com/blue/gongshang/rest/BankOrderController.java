package com.blue.gongshang.rest;

import com.blue.gongshang.service.BankOrderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Api(tags = "工商银行接口")
@RestController
@Slf4j
public class BankOrderController {

    @Resource
    private BankOrderService bankOrderService;

    //产生银行支付订单
    @PostMapping("/gongshang/makeorder.ws")
    public Map<String,Object> makeOrder(String mchtNo,String orderAmt, String openId, String orderType,String mchId) {
        return bankOrderService.makeOrder(mchtNo,orderAmt,openId,orderType,mchId);

    }


    //查询支付订单
    @PostMapping("/gongshang/getorder.ws")
    public Map<String,Object> getOrder(String mchtNo,String origReqSsn) {
        return bankOrderService.getOrder(mchtNo,origReqSsn);
    }


    //银行回调接口
    @PostMapping("/gongshang/notifyUrlServlet")
    public void callBack(HttpServletRequest req, HttpServletResponse resp){
        bankOrderService.callBack(req,resp);
    }




}
