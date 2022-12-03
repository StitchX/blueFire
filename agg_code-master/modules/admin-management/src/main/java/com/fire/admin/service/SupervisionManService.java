package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.query.PageQuery;
import com.fire.admin.vo.SupervisionManVO;

public interface SupervisionManService {

    Page<SupervisionManVO> pageSupervisionMan(PageQuery query);
}
