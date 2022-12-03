package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/25 11:26]
 */
@Data
@TableName("supervision_bureau")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupervisionBureau extends Model<SupervisionBureau> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long supervisionId;
    private String supervisionName;
    private String phone;
    private String addressId;
    private String addressName;
    private String addressDesc;
    private String createAuthor;
    private String updateAuthor;
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Long parentSupervisionId;
    @TableField(exist = false)
    private List<SupervisionBureau> childSupervisionBureau;

    @ApiModelProperty(value = "级别")
    @TableField(exist = false)
    private Integer level;

    @ApiModelProperty(value = "父级别名称")
    @TableField(exist = false)
    private String parentCategoryName;

    @ApiModelProperty(value = "省份")
    @TableField(exist = false)
    private String province;

    @ApiModelProperty(value = "城市")
    @TableField(exist = false)
    private String city;

    @ApiModelProperty(value = "地区")
    @TableField(exist = false)
    private String area;
}
