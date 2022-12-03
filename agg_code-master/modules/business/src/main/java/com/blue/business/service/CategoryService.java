package com.blue.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blue.business.vo.CategoryInfoVo;
import com.blue.business.vo.CategoryVo;
import com.fire.dto.entity.CategoryInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService extends IService<CategoryInfo> {
    List<CategoryVo> selectByParentId(Long parentCateId, String categoryName, Long categoryId);

    CategoryInfoVo categoryInfoById(Long id);
}
