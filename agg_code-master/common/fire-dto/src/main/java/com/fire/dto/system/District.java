package com.fire.dto.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @classname: District
 * @description 区域数据库实体
 * @author: liu liu
 * @create: 2020-08-13 11:48
 */
@Data
@TableName("district")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class District {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 父类编号，也就是城市省会编号
     */
    private String pid;

    /**
     * 城市名称
     */
    private String districtName;

    /**
     * 城市的类型，0是国，1是省，2是市，3是区
     */
    private Integer type;


}
