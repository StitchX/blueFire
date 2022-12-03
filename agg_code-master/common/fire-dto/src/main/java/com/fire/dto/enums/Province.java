package com.fire.dto.enums;

public enum Province {
    QG_ALL("0", "全国"),
    GD_YUE("44", "广东省"),
    HN_YU("41", "河南省"),
    NM_MENG("15", "内蒙古自治区"),
    HLJ_HEI("23", "黑龙江省"),
    XJ_JIANG("65", "新疆维吾尔自治区"),
    HB_E("42", "湖北省"),
    LN_LIAO("21", "辽宁省"),
    SD_LU("37", "山东省"),
    SX_SHAN("61", "陕西省"),
    SH_HU("31", "上海市"),
    GZ_GUI("52", "贵州省"),
    CQ_YU("50", "重庆市"),
    XZ_ZANG("54", "西藏自治区"),
    AH_WAN("34", "安徽省"),
    FJ_MIN("35", "福建省"),
    HN_XIANG("43", "湖南省"),
    HN_QIONG("46", "海南省"),
    JS_SU("32", "江苏省"),
    QH_QING("63", "青海省"),
    GX_GUI("45", "广西壮族自治区"),
    NX_NING("64", "宁夏回族自治区"),
    JX_GAN("36", "江西省"),
    ZJ_ZHE("33", "浙江省"),
    HB_JI("13", "河北省"),
    XG_GANG("81", "香港特别行政区"),
    SX_JIN("14", "山西省"),
    TW_TAI("71", "台湾省"),
    AM_AO("82", "澳门特别行政区"),
    GS_GAN("62", "甘肃省"),
    SC_CHUAN("51", "四川省"),
    YN_YUN("53", "云南省"),
    BJ_JING("11", "北京市"),
    TJ_JIN("12", "天津市"),
    JL_JI("22", "吉林省");


    private final String code;

    private final String name;


    Province(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static Province byCode(String code) {
        for (Province enums : Province.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return QG_ALL;
    }


}
