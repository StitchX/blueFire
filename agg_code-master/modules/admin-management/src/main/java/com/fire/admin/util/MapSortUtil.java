package com.fire.admin.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Description:
 * @ClassName: MapSortUtil
 * @Author: liuliu
 * @Date: 2021/11/8 18:22
 */
public class MapSortUtil {

    /***
     *@description:
     * @param map 原数据
     * @param isDesc 是否降序，true：降序，false：升序
     *@return: java.util.Map<K,V>
     *@author: liuliu
     *@date: 2021-11-08 18:21
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed())
                    .forEach(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }
}
