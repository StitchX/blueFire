package com.fire.dto.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fire.dto.constants.EsIndex;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 订单
 * @date 2022/3/10 10:37
 */
@Data
@TableName("order_list")
public class OrderList {

    @ApiModelProperty("订单号")
    @Id
    @TableId("order_id")
    @Field(type = FieldType.Long)
    private Long orderId;

    @ApiModelProperty("商户名称")
    @TableField("merchant_name")
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String merchantName;

    @ApiModelProperty("支付金额-精确到分")
    @Field(type = FieldType.Integer)
    private Integer price;

    @ApiModelProperty("状态 1.待支付 2.已支付")
    @Field(type = FieldType.Integer)
    private Integer status;

    @ApiModelProperty("商户编号")
    @TableField("merchant_id")
    @Field(type = FieldType.Long)
    private Long merchantId;

    @ApiModelProperty("柜台代码")
    @TableField("pos_id")
    @Field(type = FieldType.Keyword)
    private String posId;

    @ApiModelProperty("银行名称")
    @TableField("bank_name")
    @Field(type = FieldType.Keyword)
    private String bankName;

    @ApiModelProperty("银行编码")
    @TableField("bank_code")
    @Field(type = FieldType.Keyword)
    private String bankCode;

    @ApiModelProperty("分行代码")
    @TableField("branch_id")
    @Field(type = FieldType.Keyword)
    private String branchId;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @Field(type = FieldType.Date)
    private Date createTime;

    @ApiModelProperty("回调时间")
    @TableField("callback_time")
    @Field(type = FieldType.Date)
    private Date callbackTime;

    @ApiModelProperty("用户open_id")
    @TableField("open_id")
    @Field(type = FieldType.Keyword)
    private String openId;

    @ApiModelProperty("支付类型 1.微信 2.支付宝 3.银联")
    @Field(type = FieldType.Integer)
    private Integer type;

    @ApiModelProperty("银行订单号")
    @TableField("bank_order_id")
    @Field(type = FieldType.Keyword)
    private String bankOrderId;

    @ApiModelProperty("第三方订单号")
    @TableField("other_order_id")
    @Field(type = FieldType.Keyword)
    private String otherOrderId;

    @ApiModelProperty("银行商户号")
    @TableField("mcht_no")
    @Field(type = FieldType.Keyword)
    private String mchtNo;

    @ApiModelProperty("下单流水号")
    @TableField("make_order_sn")
    @Field(type = FieldType.Keyword)
    private String makeOrderSn;

    @ApiModelProperty("银行流水号")
    @TableField("bank_order_sn")
    @Field(type = FieldType.Keyword)
    private String bankOrderSn;

}
