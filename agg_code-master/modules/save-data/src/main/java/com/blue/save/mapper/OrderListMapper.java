package com.blue.save.mapper;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fire.dto.entity.OrderList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderListMapper extends BaseMapper<OrderList> {

    int insert(OrderList orderList);

    int update(OrderList orderList);
}
