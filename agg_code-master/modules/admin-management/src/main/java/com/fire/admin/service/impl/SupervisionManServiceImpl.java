package com.fire.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.entity.Merchant;
import com.fire.admin.entity.SupervisionBureau;
import com.fire.admin.entity.SupervisionMan;
import com.fire.admin.mapper.SupervisionManMapper;
import com.fire.admin.query.PageQuery;
import com.fire.admin.service.SupervisionBureauService;
import com.fire.admin.service.SupervisionManService;
import com.fire.admin.util.CommonUtil;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.util.SecurityUtil;
import com.fire.admin.vo.SupervisionManVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/24 15:19]
 */
@Service
@Slf4j
public class SupervisionManServiceImpl extends ServiceImpl<SupervisionManMapper, SupervisionMan> implements SupervisionManService {

    @Autowired
    private SupervisionBureauService supervisionBureauService;

    @Override
    public Page<SupervisionManVO> pageSupervisionMan(PageQuery query) {
        log.info("SupervisionManServiceImpl :: pageSupervisionMan() param is {}", query);

        Page<SupervisionManVO> page = new Page(query.getPage(), query.getSize());

        PreSecurityUser user = SecurityUtil.getUser();
        Page<SupervisionManVO> pagerResult;

        if(user != null && CollectionUtil.isNotEmpty(user.getSupervisionIds())) {
            Set<Long> supervisionQueryIds = user.getSupervisionIds();
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.in(CollectionUtil.isNotEmpty(supervisionQueryIds), "supervision_id", supervisionQueryIds);
            pagerResult = this.getBaseMapper().pageSupervisionVo(page, wrapper);
        }else {
            pagerResult = this.getBaseMapper().pageSupervisionVoAll(page);
        }


        List<SupervisionManVO> supervisionManVOList = pagerResult.getRecords();
        if(CollectionUtil.isEmpty(supervisionManVOList)){
            return pagerResult;
        }

        List<Long> supervisionIds = supervisionManVOList.stream().map(SupervisionManVO::getSupervisionId).collect(Collectors.toList());
        List<SupervisionBureau> bureauDTOList = supervisionBureauService.listBySupervisionBureauIds(supervisionIds);
        Map<Long, String> bureauNameMap = bureauDTOList.stream().collect(Collectors.toMap(SupervisionBureau::getSupervisionId, SupervisionBureau::getSupervisionName));
        supervisionManVOList.forEach(item -> {
            item.setSupervisionName(bureauNameMap.get(item.getSupervisionId()));
            item.setPhoneNum(CommonUtil.handlePhoneNumber(item.getPhoneNum()));
        });

        return pagerResult;
    }
}
