//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.blue.business.aliyun.ocr;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.fasterxml.jackson.databind.ObjectMapper;


public class FoodBusinessLicenseClient extends ApacheHttpClient{
    public final static String HOST = "foodshop.market.alicloudapi.com";
    static FoodBusinessLicenseClient instance = new FoodBusinessLicenseClient();
    public static FoodBusinessLicenseClient getInstance(){return instance;}
    public static final ObjectMapper mapper = new ObjectMapper();

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }

    public void recogniseAsync(String IMAGE_TYPE , String IMAGE , ApiCallback callback) {
        String path = "/ai_market/intelligent_food_business_license_identification/v1";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("IMAGE_TYPE" , IMAGE_TYPE , ParamPosition.BODY , true);
        request.addParam("IMAGE" , IMAGE , ParamPosition.BODY , true);
        sendAsyncRequest(request , callback);
    }

    public ApiResponse recogniseSync(String IMAGE_TYPE , String IMAGE) {
        String path = "/ai_market/intelligent_food_business_license_identification/v1";
        ApiRequest request = new ApiRequest(HttpMethod.POST_FORM , path);
        request.addParam("IMAGE_TYPE" , IMAGE_TYPE , ParamPosition.BODY , true);
        request.addParam("IMAGE" , IMAGE , ParamPosition.BODY , true);
        return sendSyncRequest(request);
    }

}