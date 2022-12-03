package com.blue.gongshang.mapper;

import  com.blue.gongshang.entity.AppWisdomGongshangPay;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 订单表(AppWisdomGongshangPay)表数据库访问层
 *
 * @author ZJQ  2022-03-16 10:42:11
 */
public interface AppWisdomGongshangPayMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param orderId 主键
     * @return 实例对象
     */
    AppWisdomGongshangPay queryById(Long orderId);



    /**
     * 通过ID查询单条数据
     *
     * @param otherOrderId 第三方订单号
     * @return 实例对象
     */
    AppWisdomGongshangPay queryByOtherOrderId(String otherOrderId);

    /**
     * 新增数据
     *
     * @param appWisdomGongshangPay 实例对象
     * @return 影响行数
     */
    int insert(AppWisdomGongshangPay appWisdomGongshangPay);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppWisdomGongshangPay> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppWisdomGongshangPay> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppWisdomGongshangPay> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppWisdomGongshangPay> entities);

    /**
     * 修改数据
     *
     * @param appWisdomGongshangPay 实例对象
     * @return 影响行数
     */
    int update(AppWisdomGongshangPay appWisdomGongshangPay);


}

