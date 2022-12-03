package com.blue.crm.vo;

import com.blue.crm.entity.BankAppointment;
import com.blue.crm.entity.FoodBusinessLicense;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fire.dto.entity.BusinessLicense;
import com.fire.dto.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/22 14:51]
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantVo extends Merchant {
    private BusinessLicense businessLicense;
    private BankAppointment appointmentBank;
    private FoodBusinessLicense foodBusinessLicense;
    private Map<Integer, AddressInfoVo> addressInfoMap;
    private Map<Integer, CategoryInfoVo> categoryInfoMap;
}
