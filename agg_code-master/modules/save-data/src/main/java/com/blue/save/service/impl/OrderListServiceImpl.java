package com.blue.save.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.dto.entity.OrderList;
import com.blue.save.mapper.OrderListMapper;
import com.blue.save.service.OrderListService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 订单服务实现
 * @date 2022/3/10 10:54
 */
@Service
public class OrderListServiceImpl extends ServiceImpl<OrderListMapper, OrderList> implements OrderListService {
    @Override
    public boolean insert(OrderList orderList) {
        return retBool(baseMapper.insert(orderList));
    }

    @Override
    public boolean update(OrderList orderList) {
        return retBool(baseMapper.update(orderList));
    }
}
