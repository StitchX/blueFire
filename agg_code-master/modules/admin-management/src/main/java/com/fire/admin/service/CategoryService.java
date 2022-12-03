package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.entity.CategoryInfo;

import java.util.List;

/**
 * @Description: 商户行业分类 service
 * @ClassName: CategoryService
 * @Author: liuliu
 * @Date: 2022/2/21 15:03
 */

public interface CategoryService extends IService<CategoryInfo> {

    /**
     * @param
     * @description: 商户行业分类树
     * @return: java.util.List<com.fire.admin.entity.CategoryInfo>
     * @author: liuliu
     * @date: 2022-02-21 15:04
     */
    List<CategoryInfo> selectCategoryList();

    /**
     * @param categoryId ： 父级分类编号
     * @description: 根据分类树获取子分类的编号
     * @return: java.util.List<java.lang.Long>
     * @author: liuliu
     * @date: 2022-02-21 17:43
     */
    List<Long> selectCategoryIds(Long categoryId);
}
