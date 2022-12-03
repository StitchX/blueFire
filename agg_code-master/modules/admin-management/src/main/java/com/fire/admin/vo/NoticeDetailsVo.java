package com.fire.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-02-21 21:54
 * @Modified By:
 */
@ApiModel("通知详情列表数据对象")
@Data
public class NoticeDetailsVo {

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("法人")
    private String representName;

    @ApiModelProperty("阅读状态:0-未读,1-已读")
    private Integer readStatus;

    @ApiModelProperty("发送时间")
    private String sendTime;

}
