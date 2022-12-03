package com.blue.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.business.mapper.BankAppointmentMapper;
import com.blue.business.service.BankAppointmentService;
import com.fire.dto.entity.BankAppointment;
import org.springframework.stereotype.Service;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/23 11:50]
 */
@Service
public class BankAppointmentServiceImpl extends ServiceImpl<BankAppointmentMapper, BankAppointment> implements BankAppointmentService {
}
