package com.blue.business.aliyun.ocr;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.blue.business.aliyun.oss.config.OssConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Service
@EnableConfigurationProperties({OssConfig.class})
public class OCRUtil {

    @Resource
    private OssConfig ossConfig;

    private static String getResultString(ApiResponse response) {
        StringBuilder result = new StringBuilder();
        result.append("Response from backend server").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if (response.getCode() != 200) {
            result.append("Error description:").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING));
        return result.toString();
    }

    @PostConstruct
    public void init() {
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey(ossConfig.getAppKey());
        httpParam.setAppSecret(ossConfig.getAppSecret());
        BusinessLicenseClient.getInstance().init(httpParam);
        FoodBusinessLicenseClient.getInstance().init(httpParam);
    }

    public String recognizeBusinessLicenseByUrl(String url) {
        BusinessLicenseClient businessLicenseClient = BusinessLicenseClient.getInstance();
        ApiResponse response = businessLicenseClient.recogniseSync(url, "1");
        String s = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
        return s;
    }

    public String recognizeFoodBusinessLicenseByUrl(String url) {
        FoodBusinessLicenseClient foodBusinessLicenseClient = FoodBusinessLicenseClient.getInstance();
        ApiResponse response = foodBusinessLicenseClient.recogniseSync("1", url);
        String s = new String(response.getBody(), SdkConstant.CLOUDAPI_ENCODING);
        return s;
    }
}
