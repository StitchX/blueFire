package com.blue.crm.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/18 15:01]
 */
@Data
@TableName("BD")
public class BD {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    @ApiModelProperty("所在部门ID")
    private Long departmentId;
    private Long sysUserId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Timestamp entryDate;
    private String creator;
    private Timestamp createTime;
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
    @ApiModelProperty("领导标记,0:BD; 1:BDM")
    private Boolean managerFlag;
    @TableField(exist = false)
    private String deptName;
    @TableField(exist = false)
    private String roleName;
    @TableField(exist = false)
    private Integer entryDays;
    @TableField(exist = false)
    private String bdmName;

}
