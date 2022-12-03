package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/25 18:20]
 */
@Data
@TableName("bank")
public class Bank {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer bankCode;
    private String bankName;
}
