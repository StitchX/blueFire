package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: 商户行业分类
 * @ClassName: CategoryInfo
 * @Author: liuliu
 * @Date: 2022/2/21 11:13
 */
@Data
@TableName("category")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryInfo {

    @ApiModelProperty(value = "分类编号")
    private Long categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = " 分类的父级id")
    private Long parentCategoryId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "修改人")
    private String updater;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp updateTime;

    @ApiModelProperty(value = " 是否删除：0-正常 1-已删除")
    private Integer isDelete;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "级别")
    @TableField(exist = false)
    private Integer level;

    @ApiModelProperty(value = "分类父级名称")
    @TableField(exist = false)
    private String parentCategoryName;

    @ApiModelProperty(value = "分类的子类别")
    @TableField(exist = false)
    private List<CategoryInfo> childenCategoryInfo;

}
