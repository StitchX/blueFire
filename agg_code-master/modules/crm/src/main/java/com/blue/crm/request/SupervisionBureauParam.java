package com.blue.crm.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("监管所查询")
@Data
public class SupervisionBureauParam {

    private Long addressId;
}
