package com.fire.dto.enums;

/**
 * @Description: 多数据源
 * @ClassName: DataSourceNames
 * @Author: liuliu
 * @Date: 2021/10/19 11:59
 */
public enum DataSourceNames {

    MASTER("master"),


    SLAVE("slave");


    public static final String DATA_MASTER = "master";
    public static final String DATA_SLAVE = "slave";

    private String key;

    DataSourceNames(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
