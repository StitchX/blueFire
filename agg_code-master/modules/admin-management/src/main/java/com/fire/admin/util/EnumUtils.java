package com.fire.admin.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fire.dto.enums.DistributePolicy;
import com.fire.dto.enums.Province;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: liuliu
 * @ClassName: EnumUtils
 * @Description: 通过省份的编号 和省份名称 获取省份信息
 * @date: 2021-05-18 15:23
 */
public class EnumUtils {

    /**
     * @Description: 获取所有省份的名称
     * @Param: []
     * @return: java.util.List<java.lang.String>
     * @Author: liuliu
     * @Date: 2021/5/18 15:31
     */
    public static List<String> getProvinces() {
        List<String> provinceList = new ArrayList<>();
        for (Province value : Province.values()) {
            String provinceName = value.getName();
            provinceList.add(provinceName);
        }
        return provinceList;
    }


    /**
     * @Description: 通过省份名称获取省份编号
     * @Param: [name]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/18 15:44
     */
    public static String convert(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Province[] values = Province.values();
        for (Province province : values) {

            if (province.getName().equals(name)) {
                return province.getCode();
            }
        }
        return null;
    }

    /**
     * @descible: 根据code 获取 value
     * @param: code
     * @return: java.lang.String
     * @author: liuliu
     * @date: 2021-06-25 14:20
     */
    public static String convertName(String code) {
        for (Province province : Province.values()) {
            if (code.equals(province.getCode())) {
                return province.getName();
            }
        }
        return null;

    }




}
