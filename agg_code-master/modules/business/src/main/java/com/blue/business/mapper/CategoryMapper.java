package com.blue.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blue.business.vo.CategoryVo;
import com.fire.dto.entity.CategoryInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface CategoryMapper extends BaseMapper<CategoryInfo> {

    List<CategoryVo> selectByParentIdAndName(@Param("pCategoryId") Long pCategoryId, @Param("categoryName") String categoryName);
}
