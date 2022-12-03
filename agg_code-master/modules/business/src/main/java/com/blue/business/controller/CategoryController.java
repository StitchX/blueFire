package com.blue.business.controller;

import com.blue.business.query.CategoryQuery;
import com.blue.business.service.CategoryService;
import com.blue.business.vo.CategoryVo;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@AllArgsConstructor
@Api(tags = "行业分类")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "下拉菜单")
    @PostMapping("/option")
    public BaseRestResponse getCategoryTree(@RequestBody CategoryQuery query) {
        log.info("CategoryService::selectByParentId: params is {}", query);
        Long parentCategoryId = query.getParentCategoryId();
        String categoryName = query.getCategoryName();
        Long categoryId = query.getCategoryId();
        List<CategoryVo> categoryVos = categoryService.selectByParentId(parentCategoryId, categoryName, categoryId);
        return new BaseRestResponse(categoryVos);
    }


}
