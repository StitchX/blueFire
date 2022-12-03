package com.blue.nongxin.config;


public class BankConfig {
    //APPID从门户获取
    private   String APPID = "197372000093002";
    //APPSECRET从门户获取
    private   String APPSECRET = "9e9f635c-044c-42f2-a75c-ca864bef9c1e";
    //OPENEPUBK--平台加密公钥
    private   String OPENEPUBK = "834782D05573D172F01DE2236DF708D2FDB270B2F383C432638F84CDA6A3EC5B127A0E6A757E8B71362F559FC3424064C2FE3EBDC054118C80B5C4DC5009959D";
    //APPDPRIK--合作方加密私钥
    private   String APPDPRIK = "D60D2C95ECD9D20E87CA29BE722F670807B1676BEB310B8D325DA172C46B43F7";
    //OPENVPUBK--平台加签公钥
    private   String OPENVPUBK = "B5829A4FD924B01F6E19DF98550E765273BACCB2E28689119791A9E19A49ECE59070E3E51F8B12933F7A2E40450A3BD59A75EFC98D84C61C171E1502A1F53284";
    //APPSPRIK--合作方加签私钥
    private   String APPSPRIK = "11E0BB2E1C6D15DA7ECB8D85EE52E39FB35FF24CB4BD2ED63EF26AEF7700F254";
    //网关地址--域名 https://open-test.scrcu.com:9051/open-gate https://open-test.scrcu.com:9051/open-gate
    private   String HTTP = "https://open.scrcu.com/open-gate";
    //接口版本号
    private   String VERSION = "1.0.0";
    //下单回调地址
    private  String CALLBACK = "http://1.13.159.173/nongxin/ordercallback.ws";

    public String getAPPID() {
        return APPID;
    }
    public void setAPPID(String aPPID) {
        APPID = aPPID;
    }
    public String getAPPSECRET() {
        return APPSECRET;
    }
    public void setAPPSECRET(String aPPSECRET) {
        APPSECRET = aPPSECRET;
    }
    public String getOPENEPUBK() {
        return OPENEPUBK;
    }
    public void setOPENEPUBK(String oPENEPUBK) {
        OPENEPUBK = oPENEPUBK;
    }
    public String getAPPDPRIK() {
        return APPDPRIK;
    }
    public void setAPPDPRIK(String aPPDPRIK) {
        APPDPRIK = aPPDPRIK;
    }
    public String getOPENVPUBK() {
        return OPENVPUBK;
    }
    public void setOPENVPUBK(String oPENVPUBK) {
        OPENVPUBK = oPENVPUBK;
    }
    public String getAPPSPRIK() {
        return APPSPRIK;
    }
    public void setAPPSPRIK(String aPPSPRIK) {
        APPSPRIK = aPPSPRIK;
    }
    public String getHTTP() {
        return HTTP;
    }
    public void setHTTP(String hTTP) {
        HTTP = hTTP;
    }
    public String getVERSION() {
        return VERSION;
    }
    public void setVERSION(String vERSION) {
        VERSION = vERSION;
    }
    public String getCALLBACK() {
        return CALLBACK;
    }
    public void setCALLBACK(String cALLBACK) {
        CALLBACK = cALLBACK;
    }

}