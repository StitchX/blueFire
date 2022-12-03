package com.blue.crm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "食品经营许可证VO")
public class FoodBusinessLicenseVo {

    @ApiModelProperty(value = "签发人")
    private String issueOfficer;

    @ApiModelProperty(value = "日常监督管理人员")
    private String regulatoryPersonnel;

    @ApiModelProperty(value = "许可证编号")
    private String licenceNumber;

    @ApiModelProperty(value = "主体业态")
    private String mainBusiness;

    @ApiModelProperty(value = "住所")
    private String officeAddress;

    @ApiModelProperty(value = "日常监督管理机构")
    private String issueAuthority;

    @ApiModelProperty(value = "经营者名称")
    private String operatorName;

    @ApiModelProperty(value = "发证机关")
    private String regulatoryAuthority;

    @ApiModelProperty(value = "法定代表人（负责人）")
    private String legalRepresentative;

    @ApiModelProperty(value = "社会信用代码（身份证号码）")
    private String creditCode;

    @ApiModelProperty(value = "经营场所")
    private String businessAddress;

    @ApiModelProperty(value = "有效期限至")
    private String validToDate;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
}
