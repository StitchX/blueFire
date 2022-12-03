package com.blue.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/25 17:57]
 */
@TableName("empty_code")
@Data
@Builder
public class EmptyCode {
    private Long merchantId;
    private Integer idType;
}
