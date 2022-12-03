package com.fire.admin.task;

import cn.hutool.core.collection.CollectionUtil;
import com.fire.admin.entity.AddressInfo;
import com.fire.admin.entity.SupervisionBureau;
import com.fire.admin.mapper.SupervisionBureauMapper;
import com.fire.admin.vo.SupervisionBureauOptionsVo;
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
 * @Description: 缓存监管所信息
 * @ClassName: SupervisionBureauData
 * @Author: liuliu
 * @Date: 2022/2/25 17:50
 */
@Slf4j
@Component
public class SupervisionBureauData {

    @Resource
    private SupervisionBureauMapper supervisionBureauMapper;

    private static Map<Long, String> supervisionBureauMap = new ConcurrentHashMap<>();

    @PostConstruct
    @Scheduled(cron = "0 0 5 * * ?")
    private void supervisionBureauAll() {
        log.info("--------------------- 开始加载监管所、监管局信息 ----------------- ");
        List<SupervisionBureauOptionsVo> options = supervisionBureauMapper.options();
        if (CollectionUtil.isNotEmpty(options)) {
            supervisionBureauMap.putAll(options.stream().collect(Collectors.toMap(SupervisionBureauOptionsVo::getSupervisionId, SupervisionBureauOptionsVo::getSupervisionName)));
        }
        log.info("--------------------- 监管所、监管局信息加载结束 ----------------- ");
    }

    public static String getSupervisionBureauName(Long supervisionId) {
        return supervisionBureauMap.get(supervisionId);
    }

}
