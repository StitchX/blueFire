package com.blue.save.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.dto.entity.OrderList;

public interface OrderListService extends IService<OrderList> {

    boolean insert(OrderList orderList);

    boolean update(OrderList orderList);
}
