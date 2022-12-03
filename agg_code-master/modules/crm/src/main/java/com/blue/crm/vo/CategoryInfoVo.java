package com.blue.crm.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.blue.crm.entity.CategoryInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

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
