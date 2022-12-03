package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.admin.entity.CategoryInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 商户分类mapper
 * @ClassName: CategoryMapper
 * @Author: liuliu
 * @Date: 2022/2/21 11:29
 */
@Repository
public interface CategoryMapper extends BaseMapper<CategoryInfo> {


    @Select("SELECT category_id , category_name , parent_category_id, sort, create_time FROM category ")
    List<CategoryInfo> queryCategoryInfos();


}
