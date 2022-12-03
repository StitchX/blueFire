package com.blue.crm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/18 14:56]
 */
@Data
@TableName("bank_appointment")
@AllArgsConstructor
@NoArgsConstructor
public class BankAppointment {
    @ApiModelProperty(value = "id主键", dataType = "string")
    private Long id;
    private Integer bankId;
    private Long merchantId;
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Timestamp appointmentTime;
    private Integer status;

}
