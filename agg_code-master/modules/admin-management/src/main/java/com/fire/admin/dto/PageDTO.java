package com.fire.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Description: 分页参数
 * @ClassName: PageDTO
 * @Author: liuliu
 * @Date: 2022/1/28 11:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class PageDTO {

    @ApiModelProperty(value = "当前页码")
    private Long current;

    @ApiModelProperty(value = "每页记录条数")
    private Long size;

}
