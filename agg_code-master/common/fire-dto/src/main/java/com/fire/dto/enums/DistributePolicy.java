package com.fire.dto.enums;

/**
 * 分发策略
 */
public enum DistributePolicy {

    COST_PRIOR(1, "成本优先"),
    THROUGHPUT_PRIOR(2, "吞吐量优先"),
    SPEED_PRIOR(3, "速度优先"),
    SUCCESS_RATE_QUEUE(4, "成功率优先"),
    WEIGHT_BALANCED(5, "权重均衡"),
    DEFAULT_POLICY(0, "默认");

    public final Integer code;

    public final String desc;

    DistributePolicy(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public static DistributePolicy getByCode(Integer code) {
        for (DistributePolicy enums : DistributePolicy.values()) {
            if (enums.code().equals(code)) {
                return enums;
            }
        }
        return DEFAULT_POLICY;
    }

}
