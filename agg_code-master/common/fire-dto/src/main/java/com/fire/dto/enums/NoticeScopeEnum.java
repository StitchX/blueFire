package com.fire.dto.enums;

/**
 * @Description: 通知范围枚举
 * @ClassName: NoticeScopeEnum
 * @Author: liuliu
 * @Date: 2022/2/25 17:28
 */
public enum NoticeScopeEnum {

    ALL_MERCHANT(1,"所有商户"),
    SCHOOL_AWAY_FIFTY_MERCHANT(2,"学校周边50米的商户"),
    AREA_CATEGORY_MERCHANT(3,"根据行业跟监管所选中商户"),
    ACCURATE_CHOOSE_MERCHANT(4,"精准选择商户");


    private final Integer scope;

    private final String msg;

    NoticeScopeEnum(Integer scope,String msg){
        this.scope=scope;
        this.msg=msg;
    }
    public Integer scope() {
        return scope;
    }

    public String msg() {
        return msg;
    }
}
