package com.fire.dto.mqDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  生成二维码必要信息实体类
 *
 * @author DK 2022/2/24 16:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeMessage {


    public static final String TOPIC = "erCode";
    /**
     * 商户id
     */
    private Long merchantId;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 监管所名称
     */
    private String supervisionName;
}
