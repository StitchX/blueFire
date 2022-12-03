package com.blue.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 区域等级
 */
@Getter
@AllArgsConstructor
public enum AreaLevelEnum {
    PROVENCE("PROVINCE", 1),
    CITY("CITY", 2),
    DISTRICT("DISTRICT", 3);

    private String name;

    private Integer level;
}
