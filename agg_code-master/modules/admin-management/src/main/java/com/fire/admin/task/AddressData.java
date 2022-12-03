package com.fire.admin.task;

import cn.hutool.core.collection.CollectionUtil;
import com.fire.admin.entity.AddressInfo;
import com.fire.admin.mapper.AddressInfoMapper;
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
 * @Description:
 * @ClassName: AddressData
 * @Author: liuliu
 * @Date: 2022/2/9 18:19
 */
@Slf4j
@Component
public class AddressData {

    @Resource
    private AddressInfoMapper addressInfoMapper;

    private static Map<String, AddressInfo> addressInfoMap = new ConcurrentHashMap<>();

    @PostConstruct
    @Scheduled(cron = "0 0 5 * * ?")
    private void addressInfoAll() {
        log.info("--------------------- 开始加载地区信息 ----------------- ");
        List<AddressInfo> addressInfos = addressInfoMapper.selectList(null);
        if (CollectionUtil.isNotEmpty(addressInfos)) {
            addressInfoMap.putAll(addressInfos.stream().collect(Collectors.toMap(AddressInfo::getCode, d -> d)));
        }
        log.info("--------------------- 地区信息加载结束 ----------------- ");
    }

    public static AddressInfo getAddressInfo(String code) {
        return addressInfoMap.get(code);
    }

}
