//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.blue.crm.aliyun.ocr;
import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.fasterxml.jackson.databind.ObjectMapper;


public class BusinessLicenseClient extends ApacheHttpClient{
    public final static String HOST = "businessid.market.alicloudapi.com";
    static BusinessLicenseClient instance = new BusinessLicenseClient();
    public static BusinessLicenseClient getInstance(){return instance;}
    public static final ObjectMapper mapper = new ObjectMapper();

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }

    public void recogniseAsync(String IMAGE , String IMAGE_TYPE , ApiCallback callback) {
        String path = "/ai_business_license/v1";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("IMAGE" , IMAGE , ParamPosition.BODY , true);
        request.addParam("IMAGE_TYPE" , IMAGE_TYPE , ParamPosition.BODY , true);
        sendAsyncRequest(request , callback);
    }

    public ApiResponse recogniseSync(String IMAGE , String IMAGE_TYPE) {
        String path = "/ai_business_license/v1";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("IMAGE" , IMAGE , ParamPosition.BODY , true);
        request.addParam("IMAGE_TYPE" , IMAGE_TYPE , ParamPosition.BODY , true);
        return sendSyncRequest(request);
    }

}