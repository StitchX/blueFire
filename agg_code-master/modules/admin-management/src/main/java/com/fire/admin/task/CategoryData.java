package com.fire.admin.task;

import cn.hutool.core.collection.CollectionUtil;
import com.fire.admin.entity.CategoryInfo;
import com.fire.admin.mapper.CategoryMapper;
import com.fire.admin.vo.SupervisionBureauOptionsVo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description: 商户行业类别缓存
 * @ClassName: CategoryData
 * @Author: liuliu
 * @Date: 2022/2/28 15:20
 */
@Slf4j
@Component
public class CategoryData {

    @Resource
    private CategoryMapper categoryMapper;

    private static Map<Long, String> categoryMap = new ConcurrentHashMap<>();

    @PostConstruct
    @Scheduled(cron = "0 0 5 * * ?")
    private void categoryAll() {
        log.info("--------------------- 开始加载商户行业类别信息 ----------------- ");
        List<CategoryInfo> infos = categoryMapper.queryCategoryInfos();
        if (CollectionUtil.isNotEmpty(infos)) {
            categoryMap.putAll(infos.stream().collect(Collectors.toMap(CategoryInfo::getCategoryId, CategoryInfo::getCategoryName)));
        }
        log.info("--------------------- 商户行业类别信息加载结束 ----------------- ");
    }

    public static String getCategory(Long categoryId) {
        return categoryMap.get(categoryId);
    }

}
