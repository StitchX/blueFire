package com.blue.consumer.service;

import com.blue.consumer.dto.*;
import com.fire.dto.response.BaseRestResponse;

/**
 * @author Dk 2022/3/8 17:14
 */
public interface ConsumerService {


     /**
      * 获取首页信息接口
      * @param homePageRequest 首页信息请求参数
      * @return 首页数据
      */
     BaseRestResponse<ConsumerHomePageResponse> getHomePageInfo(ConsumerHomePageRequest homePageRequest);

     /**
      * 经营信息查询接口
      * @param commonRequest 请求参数
      * @return 经营信息数据
      */
     BaseRestResponse<BusinessInfoResponse>  getBusinessInfo(ConsumerCommonRequest commonRequest);

     /**
      * 许可证查询接口
      * @param commonRequest 请求参数
      * @return  许可证图片url
      */
     BaseRestResponse<FoodLicenseInfoResponse> getFoodLicenseInfo(ConsumerCommonRequest commonRequest);
}
