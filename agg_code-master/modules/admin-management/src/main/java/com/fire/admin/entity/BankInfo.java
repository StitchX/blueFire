package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 18:17]
 */
@Data
@TableName("bank_info")
@ApiModel("银行信息")
public class BankInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private String custId;
    private String bankCode;
    private String payUrl;
    private String searchUrl;
    private String privateKey;
    private String pubKey;
    private String secretKey;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp updateTime;
    @TableLogic(value = "0", delval = "1")
    private Integer isDel;
    private String posId;
    private String branchId;
}
