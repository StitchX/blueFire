package com.fire.admin.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "地区表实体")
@Data
public class AddressInfo {

        @ApiModelProperty(value = "行政区划代码")
        private String code;
    
        @ApiModelProperty(value = "父级区划代码")
        private String parentCode;
    
        @ApiModelProperty(value = "名称")
        private String name;
    
        @ApiModelProperty(value = "省份直辖市")
        private String province;
    
        @ApiModelProperty(value = "城市")
        private String city;
    
        @ApiModelProperty(value = "区县")
        private String area;
    
        @ApiModelProperty(value = "乡镇")
        private String town;
    
        @ApiModelProperty(value = "地区级别【1、省份，2、城市、3,、区县、4、乡镇】")
        private Integer level;
    


}
