package com.blue.gongshang.service.impl;


import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.blue.gongshang.config.BankConfig;
import com.blue.gongshang.entity.AppWisdomGongshangPay;
import com.blue.gongshang.mapper.AppWisdomGongshangPayMapper;
import com.blue.gongshang.service.BankOrderService;
import com.icbc.api.DefaultIcbcClient;
import com.icbc.api.IcbcConstants;
import com.icbc.api.UiIcbcClient;
import com.icbc.api.request.CardbusinessAggregatepayB2cOnlineOrderqryRequestV1;
import com.icbc.api.request.CardbusinessAggregatepayB2cOnlineUiConsumepurchaseshowpayRequestV1;
import com.icbc.api.response.CardbusinessAggregatepayB2cOnlineOrderqryResponseV1;
import com.icbc.api.utils.IcbcEncrypt;
import com.icbc.api.utils.IcbcSignature;
import com.icbc.api.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : [QGC]
 * @version : [v1.0]
 * @createTime : [2022/2/23 13:23]
 */
@Service
@Slf4j
public class BankOrderServiceImpl implements BankOrderService {

    private static final BankConfig config=new BankConfig();

    @Resource
    private AppWisdomGongshangPayMapper appWisdomGongshangPayMapper;


    @Override
    public Map<String,Object> makeOrder(String mchtNo,String orderAmt, String openId, String orderType,String mchId) {
        HashMap<String, Object> resMap = new HashMap<>();
        try {
            UiIcbcClient client = new UiIcbcClient(config.getAPPID(), IcbcConstants.SIGN_TYPE_RSA2, config.getMYPRIVATEKEY(), IcbcConstants.CHARSET_UTF8);
            CardbusinessAggregatepayB2cOnlineUiConsumepurchaseshowpayRequestV1 request = new CardbusinessAggregatepayB2cOnlineUiConsumepurchaseshowpayRequestV1();
            //根据测试环境和生产环境替换相应ip和端口
            request.setServiceUrl("https://gw.open.icbc.com.cn/ui/cardbusiness/aggregatepay/b2c/online/ui/consumepurchaseshowpay/V1");
            CardbusinessAggregatepayB2cOnlineUiConsumepurchaseshowpayRequestV1.CardbusinessAggregatepayB2cOnlineUiConsumepurchaseshowpayRequestV1Biz bizContent = new CardbusinessAggregatepayB2cOnlineUiConsumepurchaseshowpayRequestV1.CardbusinessAggregatepayB2cOnlineUiConsumepurchaseshowpayRequestV1Biz();
            request.setBizContent(bizContent);
            //请对照接口文档用bizContent.setxxx()方法对业务上送数据进行赋值
            bizContent.setAttach("");
            bizContent.setBody("智慧放心码");
            bizContent.setExpireTime("120");
            bizContent.setIcbc_appid(config.getAPPID());
            bizContent.setMer_id(mchtNo); //231704030062
            bizContent.setMer_prtcl_no(mchtNo);
            bizContent.setMer_acct("");
            bizContent.setNotify_type("");
            bizContent.setNotify_url(config.getCALLBACK());
            bizContent.setOpenId("");
            //金额乘100
            BigDecimal BigDecAmt = new BigDecimal(orderAmt);
            BigDecimal outBigDecAmt = BigDecAmt.multiply(new BigDecimal(100));
            String orderAmt100 = outBigDecAmt.stripTrailingZeros().toPlainString();
            bizContent.setOrder_amt(orderAmt100);
            String uuid = getUUID();
            bizContent.setOut_trade_no(uuid);
            bizContent.setPay_limit("");
            bizContent.setResult_type("");
            bizContent.setReturn_url("");
            bizContent.setSaledepname("智慧放心码");
            bizContent.setSubject("智慧放心码");
            bizContent.setShop_appid("");
            bizContent.setOrder_apd_inf("");

            String str = client.buildPostForm(request);
            resMap.put("respCode","00");

            resMap.put("respMsg","成功");
            resMap.put("origReqSsn",uuid);
            resMap.put("respContent",str);
            log.info("工商银行下单：" + resMap);

            //订单入库
            AppWisdomGongshangPay appWisdomGongshangPay = new AppWisdomGongshangPay();
            appWisdomGongshangPay.setBankCode("6");
            appWisdomGongshangPay.setPrice(Double.valueOf(orderAmt));
            appWisdomGongshangPay.setStatus(1);
            appWisdomGongshangPay.setMchtNo(mchtNo);
            if (Strings.isNotBlank(mchId)) {
                appWisdomGongshangPay.setMerchantId(Long.valueOf(mchId));
            }
            appWisdomGongshangPay.setCreateTime(new Date());
            appWisdomGongshangPay.setOpenId(openId);
            if (Strings.isNotBlank(orderType)) {
                appWisdomGongshangPay.setType(Integer.valueOf(orderType));
            }
            appWisdomGongshangPay.setOtherOrderId(uuid);
            appWisdomGongshangPayMapper.insert(appWisdomGongshangPay);

        } catch (Exception e){
            log.error("工商银行下单错误",e);
            resMap.put("respCode","99");
            resMap.put("respMsg","下单数据出错");
            resMap.put("origReqSsn","");
            resMap.put("respContent","");
        }
        return resMap;
    }

    @Override
    public Map<String,Object> getOrder(String mchtNo,String origReqSsn) {
        HashMap<String, Object> resMap = new HashMap<>();
        try {
            DefaultIcbcClient client = new DefaultIcbcClient(config.getAPPID(), IcbcConstants.SIGN_TYPE_RSA2, config.getMYPRIVATEKEY(), config.getAPIGWPUBLICKEY());
            CardbusinessAggregatepayB2cOnlineOrderqryRequestV1 request = new CardbusinessAggregatepayB2cOnlineOrderqryRequestV1();
            //根据测试环境和生产环境替换相应ip和端口
            request.setServiceUrl("https://gw.open.icbc.com.cn/api/cardbusiness/aggregatepay/b2c/online/orderqry/V1");
            CardbusinessAggregatepayB2cOnlineOrderqryRequestV1.CardbusinessAggregatepayB2cOnlineOrderqryRequestV1Biz bizContent = new CardbusinessAggregatepayB2cOnlineOrderqryRequestV1.CardbusinessAggregatepayB2cOnlineOrderqryRequestV1Biz();
            request.setBizContent(bizContent);
            //请对照接口文档用bizContent.setxxx()方法对业务上送数据进行赋值
            bizContent.setMer_id(mchtNo);
            bizContent.setOut_trade_no(origReqSsn);
            bizContent.setOrder_id("");
            bizContent.setDeal_flag("0");
            bizContent.setIcbc_appid(config.getAPPID());

            CardbusinessAggregatepayB2cOnlineOrderqryResponseV1 response;

            response = client.execute(request, System.currentTimeMillis() + "");//msgId消息通讯唯一编号，要求每次调用独立生成，APP级唯一
            log.info("工商银行查询订单:" + JSON.toJSONString(response));
            if (response.getReturnCode() == 0) {
                // 6、业务成功处理，请根据接口文档用response.getxxx()获取同步返回的业务数据
                resMap.put("respCode","00");
                resMap.put("respMsg", response.getReturnMsg());
                resMap.put("txnType", "01");
                resMap.put("txnAmt", Double.parseDouble(response.getTotal_amt()) / 100);
                if ("0".equals(response.getPay_status())){
                    resMap.put("orderState", "02");
                } else if ("1".equals(response.getPay_status())){
                    resMap.put("orderState", "03");
                } else {
                    resMap.put("orderState", "01");
                }

            } else {
                // 失败
                resMap.put("respCode",response.getReturnCode());
                resMap.put("respMsg", response.getReturnMsg());
                resMap.put("txnType", "");
                resMap.put("txnAmt", "");
                resMap.put("orderState", "");
            }

            } catch (Exception e) {
                log.error("工商银行订单查询错误", e);
                resMap.put("respCode", "99");
                resMap.put("respMsg", "查询订单失败错误");
                resMap.put("txnType", "");
                resMap.put("txnAmt", "");
                resMap.put("orderState", "");
            }
        return resMap;
    }


    @Override
    public void callBack(HttpServletRequest req, HttpServletResponse resp){
        Map<String, String> params=new HashMap<>();
        String from=req.getParameter("from");
        String api=req.getParameter("api");
        String app_id=req.getParameter("app_id");
        String charset=req.getParameter("charset");
        String format=req.getParameter("format");
        String encrypt_type=req.getParameter("encrypt_type");
        String timestamp=req.getParameter("timestamp");
        String biz_content=req.getParameter("biz_content");
        String sign_type=req.getParameter("sign_type");
        String sign=req.getParameter("sign");
        params.put("from", from);
        params.put("api", api);
        params.put("app_id", app_id);
        params.put("charset", charset);
        params.put("format", format);
        params.put("encrypt_type", encrypt_type);
        params.put("timestamp", timestamp);
        params.put("biz_content", biz_content);
        params.put("sign_type", sign_type);//目前上行网关签名暂时仅支持RSA
        PrintWriter out = null;
        log.info("工商银行回调数据：" + params);
        try {
            //**********验证工行上行网关RSA签名**********/
            String path="/gongshang/notifyUrlServlet";
            String signStr= WebUtils.buildOrderedSignStr(path, params);
            String results;
            String responseBizContent;
            boolean flag= IcbcSignature.verify(signStr, sign_type, config.getAPIGWPUBLICKEY(), charset, sign);
            if (!flag) {
                responseBizContent= "{\"return_code\":-12345,\"return_msg\":\"icbc sign not pass.\"}";
            }else
            {

                //**********biz_content解密**********/

                if ("AES".equals(encrypt_type))
                {
                    String theKey="12345678901234567890123456789012";
                    biz_content= IcbcEncrypt.decryptContent(biz_content, encrypt_type, theKey, charset);
                }

                //**********合作方/分行 业务逻辑处理**********/
                @SuppressWarnings("unchecked")
                Map<String, Object> respMap = (Map<String, Object>) JSON.parse(biz_content);
                //业务请求字段获取

                String msg_id = respMap.get("msg_id").toString();
                String orderId = respMap.get("order_id").toString();
                String outTradeNo = respMap.get("out_trade_no").toString();
                //更新数据库
                AppWisdomGongshangPay appWisdomGongshangPay = appWisdomGongshangPayMapper.queryByOtherOrderId(outTradeNo);
                if (appWisdomGongshangPay != null){
                    appWisdomGongshangPay.setCallbackTime(new Date());
                    appWisdomGongshangPay.setBankOrderId(orderId);
                    if ( "0".equals(respMap.get("return_code").toString())){
                        appWisdomGongshangPay.setStatus(2);
                    } else {
                        appWisdomGongshangPay.setStatus(3);
                    }
                    appWisdomGongshangPayMapper.update(appWisdomGongshangPay);
                }

                //业务返回参数设置
                int return_code=0;
                String return_msg="success.";
                responseBizContent="{\"return_code\":"+return_code+",\"return_msg\":\""+return_msg+"\",\"msg_id\":\""+msg_id+"\","
                        +"\"busi_param_rp\":\"thisisresponseparameter\"}";

                //**********response_biz_content加密**********/

                if ("AES".equals(encrypt_type))
                {
                    String theKey="12345678901234567890123456789012";
                    responseBizContent=IcbcEncrypt.encryptContent(responseBizContent, encrypt_type, theKey, charset);
                    responseBizContent="\""+responseBizContent+"\"";
                }

            }

            //**********商户对消息返回响应进行签名，签名方式需与在API平台登记APP的sign_type保持一致**********/

            //2、商户以RSA签名为例，如下：其中，priKey为商户私钥；
            signStr="\"response_biz_content\":"+responseBizContent+","+"\"sign_type\":"+"\"RSA\"";
            sign=IcbcSignature.sign(signStr, "RSA", config.getMYPRIVATEKEY(),
                    charset,"12345678");
            results="{"+signStr+",\"sign\":\""+sign+"\"}";
            resp.setContentType("application/json; charset=utf-8");
            out = resp.getWriter();
            out.write(results);
        }catch (Throwable e) {
            log.error("工商银行回调错误：",e);
            out.write(e.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }



    public static String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }



    }
