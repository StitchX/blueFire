package com.fire.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fire.dto.enums.Status.SUCCESS;


/**
 * 基础无数据返回的响应
 *
 * @author ZJQ 2021年5月14日14:50:33
 */
@Getter
@Setter
public class BaseResponse {

    @ApiModelProperty(value = "接口状态", example = "200")
    private String status = SUCCESS.status();
    @ApiModelProperty(value = "接口状态信息", example = "成功")
    private String message = SUCCESS.message();

    /**
     * 空构造方法，适用场景，无数据返回成功
     */
    public BaseResponse() {
    }

    /**
     * 带状态构造方法，适用场景，返回错误信息
     *
     * @param status  状态
     * @param message 错误信息
     */
    public BaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }


}
