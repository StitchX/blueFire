package com.fire.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.PageDTO;
import com.fire.admin.dto.SupervisionBureauDTO;
import com.fire.admin.entity.SupervisionBureau;
import com.fire.admin.vo.NotiDetaVo;
import com.fire.admin.vo.SupervisionBureauOptionsVo;
import com.fire.admin.vo.SupervisionBureauVo;

import java.util.List;
import java.util.Set;

public interface SupervisionBureauService extends IService<SupervisionBureau> {

    List<SupervisionBureau> listBySupervisionBureauIds(List<Long> ids);

    List<SupervisionBureauOptionsVo> listOptions();

    /**
     * @param supervisionId
     * @description: 根据监管所编号获取其下属的监管所监管所编号
     * @return: java.util.List<java.lang.Long>
     * @author: liuliu
     * @date: 2022-01-25 19:32
     */
    Set<Long> querySupervisionBureau(Long supervisionId);
   /* Set<Long> querySupervisionBureau(Long supervisionId, boolean flag);*/


    SupervisionBureau getSupervisionBureauById(Long id);


    /***
     *@description: 获取所有监管所、监管局的树
     * @param flag : true ： 包括本级  false ： 不包括本级
     *@return: java.util.List<cn.hutool.core.lang.tree.Tree < java.lang.Object>>
     *@author: liuliu
     *@date: 2022-01-27 17:35
     */
    List<SupervisionBureau> queryAllSupervisionBureauTree();
    // List<Tree<Object>> queryAllSupervisionBureauTree(boolean flag);

    /**
     * @param
     * @description: 当前登录账户下的所有能看到的监管所
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.fire.admin.vo.SupervisionBureauVo>
     * @author: liuliu
     * @date: 2022-01-27 18:27
     */
    IPage<SupervisionBureauVo> querySupervisionBureauByUser(PageDTO page);

    /**
     * @param supervisionBureauDTO
     * @description: 监管所、监管局新增
     * @return: int
     * @author: liuliu
     * @date: 2022-02-10 12:07
     */
    boolean insertSupervisionBureau(SupervisionBureauDTO supervisionBureauDTO);

    /**
     * @param supervisionBureauDTO
     * @description: 修改监管所或者监管局信息
     * @return: boolean
     * @author: liuliu
     * @date: 2022-02-10 14:45
     */
    boolean updateSupervisionBureau(SupervisionBureauDTO supervisionBureauDTO);

    /**
     * @param
     * @description: 当前账户下的所有监管所
     * @return: java.util.List<com.fire.admin.vo.NotiDetaVo.NoticeSupervisionBureau>
     * @author: liuliu
     * @date: 2022-03-02 14:08
     */
    List<NotiDetaVo.NoticeSupervisionBureau> accountSupervisionBureau();

}
