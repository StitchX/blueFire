package com.blue.crm.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.crm.entity.SupervisionBureau;
import com.blue.crm.mapper.SupervisionBureauMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/24 15:19]
 */
@Service
@Slf4j
public class SupervisionBureauService extends ServiceImpl<SupervisionBureauMapper, SupervisionBureau> {

    public List<SupervisionBureau> selectByAddressId(Long addressId) {
        return baseMapper.selectByAddressId(addressId);
    }
}
