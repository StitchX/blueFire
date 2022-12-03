package com.fire.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * rest接口标准返回类型
 *
 * @author ZJQ 2021年5月14日11:27:25
 */
@Getter
@Setter
public class BaseRestResponse<T> extends BaseResponse {

    @ApiModelProperty(value = "接口返回数据", example = "成功")
    private T data;

    /**
     * 空构造方法，适用场景，无数据返回成功
     */
    public BaseRestResponse() {
    }

    /**
     * 带数据构造方法，适用场景，有数据返回成功
     *
     * @param data 泛型的Data
     */
    public BaseRestResponse(T data) {
        this.data = data;
    }

    /**
     * 带状态构造方法，适用场景，返回错误信息
     *
     * @param status  状态
     * @param message 错误信息
     */
    public BaseRestResponse(String status, String message) {
        super(status, message);
    }

    /**
     * 带状态和数据构造方法，适用场景，返回错误信息且带数据，这个比较少用
     *
     * @param status  状态
     * @param message 错误信息
     * @param data    泛型的数据
     */
    public BaseRestResponse(String status, String message, T data) {
        super(status, message);
        this.data = data;
    }

    public static BaseRestResponse error(String msg) {
        return error("500", msg);
    }

    public static BaseRestResponse error(String status, String message) {
        BaseRestResponse response = new BaseRestResponse();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }


}
