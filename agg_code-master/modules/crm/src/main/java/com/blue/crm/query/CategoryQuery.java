package com.blue.crm.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("行业分类查询参数")
public class CategoryQuery {
    @ApiModelProperty("父级id")
    private Long parentCategoryId = 0L;

    @ApiModelProperty("行业名称")
    private String categoryName;

    @ApiModelProperty("已选择行业id")
    private Long categoryId;
}
