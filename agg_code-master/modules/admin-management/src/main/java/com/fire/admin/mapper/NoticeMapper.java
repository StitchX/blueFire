package com.fire.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fire.admin.dto.NoticeDto;
import com.fire.admin.entity.NoticeInfo;
import com.fire.admin.vo.NoticeVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Description: 通知mapper
 * @ClassName: NoticeMapper
 * @Author: liuliu
 * @Date: 2022/2/21 17:16
 */
@Repository
public interface NoticeMapper extends BaseMapper<NoticeInfo> {

    /**
     * 分页获取通知列表
     *
     * @param page
     * @param param
     */
    IPage<NoticeVo> queryNoticeList(Page page, NoticeDto param);

    /**
     * 根据通知编号获取通知详情
     *
     * @param noticeId
     */
    NoticeInfo queryDetailsById(Long noticeId);

    /**
     * @param noticeId
     * @description: 发起通知获取条件
     * @return: com.fire.admin.dto.NoticeDto
     * @author: liuliu
     * @date: 2022-03-04 11:34
     */
    @Select("select notice_id,notice_condition,supervision_id from notice_info where notice_id=#{noticeId} ")
    NoticeInfo sendNoticeById(Long noticeId);
}
