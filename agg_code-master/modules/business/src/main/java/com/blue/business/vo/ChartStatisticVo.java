package com.blue.business.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 柱状、折线图
 * @date 2022/3/24 17:53
 */
@Data
@AllArgsConstructor
public class ChartStatisticVo {

    private List<String> xAxis;

    private List<Object> yAxis;
}
