package com.blue.business.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.blue.business.security.util.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description:自定义公共字段填充处理器
 * @date: 2021-07-04 16:04
 */
@Component
public class FillDataHandler implements MetaObjectHandler {

    /**
     * 插入操作自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("merchantId", SecurityUtil.getUser().getMerchantId(), metaObject);

        setFieldValByName("createTime", new Date(), metaObject);
    }

    /**
     * 更新操作自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        setFieldValByName("updateTime", new Date(), metaObject);
    }
}
