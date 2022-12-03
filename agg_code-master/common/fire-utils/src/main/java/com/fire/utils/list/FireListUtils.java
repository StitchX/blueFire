package com.fire.utils.list;

import java.util.ArrayList;
import java.util.List;

public class FireListUtils {



    /**
     * @param list 需要拆分的list
     * @param len  每个list长度
     * @return List
     */
    public static <V> List<List<V>> splitList(List<V> list, int len) {
        //为空或者长度小于1 直接返回空
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }

        int size = list.size();
        int count = (size + len - 1) / len;

        //返回结果集
        List<List<V>> result = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            List<V> subList = list.subList(i * len, (Math.min((i + 1) * len, size)));
            result.add(subList);
        }
        return result;
    }

}
