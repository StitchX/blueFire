package com.blue.nongxin.mapper;

import com.blue.nongxin.entity.AppWisdomNongxinPay;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 订单表(AppWisdomNongxinPay)表数据库访问层
 *
 * @author ZJQ  2022-03-01 17:36:01
 */
public interface AppWisdomNongxinPayMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param orderId 主键
     * @return 实例对象
     */
    AppWisdomNongxinPay queryById(Long orderId);

    AppWisdomNongxinPay queryBySsn(String ssn);

    /**
     * 新增数据
     *
     * @param appWisdomNongxinPay 实例对象
     * @return 影响行数
     */
    int insert(AppWisdomNongxinPay appWisdomNongxinPay);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppWisdomNongxinPay> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppWisdomNongxinPay> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppWisdomNongxinPay> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppWisdomNongxinPay> entities);

    /**
     * 修改数据
     *
     * @param appWisdomNongxinPay 实例对象
     * @return 影响行数
     */
    int update(AppWisdomNongxinPay appWisdomNongxinPay);


}

