package com.fire.admin.rest;

import com.fire.admin.service.CategoryService;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 商户行业分类接口
 * @ClassName: CategoryController
 * @Author: liuliu
 * @Date: 2022/2/21 15:57
 */
@RestController
@RequestMapping("/category")
@AllArgsConstructor
@Api(tags = "商户行业分类接口")
public class CategoryController {

    private CategoryService categoryService;

    @ApiOperation(value = "商户行业分类树")
    @PostMapping("/tree")
    public BaseRestResponse getCategoryTree() {
        return new BaseRestResponse(categoryService.selectCategoryList());
    }


}
