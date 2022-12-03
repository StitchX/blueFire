package com.blue.code.mapper;

import com.blue.code.entity.Merchant;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 商户表(Merchant)表数据库访问层
 *
 * @author ZJQ  2022-02-25 10:24:17
 */
public interface MerchantMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param merchantId 主键
     * @return 实例对象
     */
    Merchant queryById(Long merchantId);

    /**
     * 新增数据
     *
     * @param merchant 实例对象
     * @return 影响行数
     */
    int insert(Merchant merchant);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Merchant> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Merchant> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Merchant> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<Merchant> entities);

    /**
     * 修改数据
     *
     * @param merchant 实例对象
     * @return 影响行数
     */
    int update(Merchant merchant);


}

