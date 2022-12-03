package com.blue.crm.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blue.crm.dto.LoginUser;
import com.blue.crm.entity.BD;
import com.blue.crm.enums.BDEnum;
import com.blue.crm.mapper.BDMapper;
import com.blue.crm.util.SecurityUtil;
import com.fire.dto.system.SysDept;
import com.fire.dto.system.SysRole;
import com.fire.dto.system.SysRoleDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/21 15:00]
 */
@Service
public class BDService extends ServiceImpl<BDMapper, BD> {

    @Autowired
    private BDService bdService;

    @Autowired
    private SysDeptService sysDeptService;

    public BD getLoginBDInfo() {

        LoginUser user = SecurityUtil.getUser();
        BD bd = bdService.getById(user.getBdId());

        //计算入职时间
        if (bd.getEntryDate() != null) {
            Timestamp entryTime = bd.getEntryDate();
            Long now = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
            Long entrySecond = entryTime.toLocalDateTime().toEpochSecond(ZoneOffset.ofHours(8));
            Long entryDays = (now - entrySecond) / (60 * 60 * 24);
            bd.setEntryDays(entryDays.intValue());
        }
        bd.setRoleName(BDEnum.BD.getDesc());

        SysDept sysDept = sysDeptService.getById(bd.getDepartmentId());
        if(ObjectUtil.isNotEmpty(sysDept)) {
            bd.setDeptName(sysDept.getName());
            LambdaQueryWrapper BDMWrapper = new LambdaQueryWrapper<BD>()
                    .eq(BD::getManagerFlag, 1)
                    .eq(BD::getDepartmentId, sysDept.getDeptId());
            List<BD> bdm = bdService.list(BDMWrapper);
            if(CollectionUtil.isNotEmpty(bdm)) {
                bd.setBdmName(bdm.get(0).getName());
            }
        }
        return bd;
    }

}
