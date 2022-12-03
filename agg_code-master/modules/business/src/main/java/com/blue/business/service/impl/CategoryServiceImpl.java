package com.blue.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.converter.CategoryInfoConverter;
import com.blue.business.mapper.CategoryMapper;
import com.blue.business.service.CategoryService;
import com.blue.business.vo.CategoryInfoVo;
import com.blue.business.vo.CategoryVo;
import com.fire.dto.entity.CategoryInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryInfo> implements CategoryService {


    public List<CategoryVo> selectByParentId(Long parentCateId, String categoryName, Long categoryId) {
        if (Objects.nonNull(categoryId)) {
            if (categoryId == -1) {
                categoryName = "食品";
            }else if(categoryId == -2) {
                categoryName = "非餐饮";
            }else if (categoryId > 0) {
                //已选择具体行业通过第一级行业父级来确定是餐饮还是非餐饮
                while (categoryId != 0 && categoryId != 1) {
                    CategoryInfo categoryInfo = this.getBaseMapper().selectById(categoryId);
                    categoryId = categoryInfo.getParentCategoryId();
                    if (categoryId == 1) {
                        categoryName = "食品";
                    }else if(categoryId == 0) {
                        categoryName = "非餐饮";
                    }
                }
            }
            parentCateId = 0l;
        }
        return baseMapper.selectByParentIdAndName(parentCateId, categoryName);
    }

    //递归获取行业类型信息
    public CategoryInfoVo categoryInfoById(Long id) {
        log.info("CategoryService::categoryInfoById: params is {}", id);
        LambdaQueryWrapper wrapper = new LambdaQueryWrapper<CategoryInfo>()
                .eq(id != null, CategoryInfo::getCategoryId, id);
        CategoryInfo categoryInfo = this.getOne(wrapper);
        return CategoryInfoConverter.INSTANCE.entity2Vo(categoryInfo);
    }
}
