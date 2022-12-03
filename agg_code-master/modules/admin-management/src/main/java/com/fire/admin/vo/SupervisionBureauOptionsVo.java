package com.fire.admin.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/27 14:49]
 */

@TableName("supervision_bureau")
@Data
public class SupervisionBureauOptionsVo {
    private Long supervisionId;
    private String supervisionName;
    private Long parentSupervisionId;
}
