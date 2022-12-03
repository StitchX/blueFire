package com.blue.business.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/4 11:54]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInfoVo {

    @ApiModelProperty(value = "分类编号")
    private Long categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = " 分类的父级id")
    private Long parentCategoryId;

}
