package com.fire.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/21 14:22]
 */

@ApiModel("银行信息表")
@Data
@TableName("bank_info")
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
    private Timestamp createTime;
    private Timestamp updateTime;
    @TableLogic(value = "0", delval = "1")
    private Integer isDel;
    private String posId;
    private String branchId;
}
