package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 18:26]
 */
@ApiModel("商户扫码")
@Data
@TableName("scan_code_count")
public class ScanCodeCount {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private String avatar;
    private String openId;
    private String nickName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createTime;
    private Integer terminal;
}
