package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;


@Data
@TableName("supervision_man")
public class SupervisionMan {
    /**
     * 监管员编号
     */
    @TableId(type = IdType.AUTO)
    private Long supervisionManId;
    /**
     * 监管员名称
     */
    private String supervisionManName;
    /**
     * 监管员电话
     */
    private String phoneNum;
    /**
     * 监管所编号
     */
    private String supervisionId;
    /**
     * 监管员创建人
     */
    private String createAuthor;
    /**
     * 监管员修改人
     */
    private String updateAuthor;
    /**
     * 状态, 0:正常 1：删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;
    /**
     * 监管员创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createdTime;
    /**
     * 监管员修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp updateTime;

}
