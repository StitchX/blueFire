package com.fire.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.entity.CategoryInfo;
import com.fire.admin.mapper.CategoryMapper;
import com.fire.admin.service.CategoryService;
import com.fire.admin.util.PreUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 商户行业分类实现类
 * @ClassName: CategoryServiceImpl
 * @Author: liuliu
 * @Date: 2022/2/21 15:05
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryInfo> implements CategoryService {

    /**
     * @description: 商户行业分类树
     * @return: java.util.List<com.fire.admin.entity.CategoryInfo>
     * @author: liuliu
     * @date: 2022-02-21 15:06
     */
    @Override
    public List<CategoryInfo> selectCategoryList() {
        List<CategoryInfo> categoryAll = baseMapper.queryCategoryInfos();
        List<CategoryInfo> categoryInfos = categoryAll.stream()
                .filter(category -> category.getParentCategoryId() == 0L || ObjectUtil.isNull(category.getCategoryId()))
                .peek(category -> category.setLevel(0))
                .collect(Collectors.toList());
        PreUtil.findChildren(categoryInfos, categoryAll);
        return categoryInfos;
    }

    /**
     * @param categoryId ： 父级分类编号
     * @description: 根据分类树获取子分类的编号
     * @return: java.util.List<java.lang.Long>
     * @author: liuliu
     * @date: 2022-02-21 17:44
     */
    @Override
    public List<Long> selectCategoryIds(Long categoryId) {
        CategoryInfo categoryInfo = getParentCategoryInfo(categoryId);
        List<Long> childrenList = Lists.newArrayList();
        if (ObjectUtil.isNotNull(categoryInfo)) {
            childrenList.add(categoryInfo.getCategoryId());
            addCategoryIds(childrenList, categoryInfo);
        }
        return childrenList;
    }

    /**
     *@description: 获取子分类的编号
     * @param childrenList
     * @param categoryInfo
     *@return: void
     *@author: liuliu
     *@date: 2022-02-21 18:23
    */
    private void addCategoryIds(List<Long> childrenList, CategoryInfo categoryInfo) {
        List<CategoryInfo> childenCategoryInfo = categoryInfo.getChildenCategoryInfo();
        if (CollectionUtil.isNotEmpty(childenCategoryInfo)) {
            childenCategoryInfo.forEach(ca -> {
                childrenList.add(ca.getCategoryId());
                addCategoryIds(childrenList, ca);
            });
        }
    }


    private CategoryInfo getParentCategoryInfo(Long categoryId) {
        List<CategoryInfo> categoryInfos = baseMapper.queryCategoryInfos();
        Map<Long, CategoryInfo> map = categoryInfos.stream().collect(Collectors.toMap(CategoryInfo::getCategoryId, d -> d));
        for (CategoryInfo info : map.values()) {
            CategoryInfo parentInfo = map.get(info.getParentCategoryId());
            if (ObjectUtil.isNotNull(parentInfo)) {
                List<CategoryInfo> children = CollectionUtil.isEmpty(parentInfo.getChildenCategoryInfo()) ? new ArrayList<CategoryInfo>() : parentInfo.getChildenCategoryInfo();
                children.add(info);
                parentInfo.setChildenCategoryInfo(children);
            }
        }
        return map.get(categoryId);
    }


}
