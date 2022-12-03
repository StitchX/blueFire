package com.blue.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.crm.entity.CategoryInfo;
import com.blue.crm.vo.CategoryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryMapper extends BaseMapper<CategoryInfo> {

    List<CategoryVo> selectByParentIdAndName(@Param("pCategoryId") Long pCategoryId, @Param("categoryName") String categoryName);
}
