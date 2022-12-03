package com.fire.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.converter.SupervisionBureauConverter;
import com.fire.admin.dto.PageDTO;
import com.fire.admin.dto.SupervisionBureauDTO;
import com.fire.admin.entity.AddressInfo;
import com.fire.admin.entity.SupervisionBureau;
import com.fire.admin.mapper.SupervisionBureauMapper;
import com.fire.admin.service.SupervisionBureauService;
import com.fire.admin.task.AddressData;
import com.fire.admin.task.SupervisionBureauData;
import com.fire.admin.util.PreSecurityUser;
import com.fire.admin.util.PreUtil;
import com.fire.admin.util.SecurityUtil;
import com.fire.admin.vo.NotiDetaVo;
import com.fire.admin.vo.SupervisionBureauVo;
import com.fire.admin.vo.SupervisionBureauOptionsVo;
import com.fire.common.exception.BaseException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/24 15:19]
 */
@Service
@Slf4j
public class SupervisionBureauServiceImpl extends ServiceImpl<SupervisionBureauMapper, SupervisionBureau> implements SupervisionBureauService {
    @Override
    public List<SupervisionBureau> listBySupervisionBureauIds(List<Long> ids) {
        return this.listByIds(ids);
    }

    @Override
    public SupervisionBureau getSupervisionBureauById(Long id) {
        LambdaQueryWrapper<SupervisionBureau> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotEmpty(id), SupervisionBureau::getSupervisionId, id);
        return this.getOne(queryWrapper);
    }


    @Override
    public List<SupervisionBureauOptionsVo> listOptions() {
        PreSecurityUser user = (PreSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            return SupervisionBureauConverter.INSTANCE.entities2OptionsVos(this.list());
        }
        Long supervisionId = user.getSupervisionId();
        LambdaQueryWrapper wrapper = new LambdaQueryWrapper<SupervisionBureau>()
                .eq(SupervisionBureau::getSupervisionId, supervisionId)
                .or().eq(SupervisionBureau::getParentSupervisionId, supervisionId);
        List<SupervisionBureau> supervisionBureauList = this.list(wrapper);
        return SupervisionBureauConverter.INSTANCE.entities2OptionsVos(supervisionBureauList);
    }


    /**
     * @param supervisionId
     * @description: 根据监管所编号获取其下属的监管所监管所编号
     * @return: java.util.List<java.lang.Long>
     * @author: liuliu
     * @date: 2022-01-25 19:34
     */
    @Override
    public Set<Long> querySupervisionBureau(Long supervisionId) {
        SupervisionBureau parentSupervisionBureau = getParentSupervisionBureau(supervisionId);
        Set<Long> childrenSet = new HashSet<>();
        if (ObjectUtil.isNotNull(parentSupervisionBureau)) {
            childrenSet.add(parentSupervisionBureau.getSupervisionId());
            addSupervisionIds(childrenSet, parentSupervisionBureau);
        }
        return childrenSet;
    }

    private void addSupervisionIds(Set<Long> childrenSet, SupervisionBureau parentSupervisionBureau) {
        List<SupervisionBureau> childSupervisionBureau = parentSupervisionBureau.getChildSupervisionBureau();
        if (CollectionUtil.isNotEmpty(childSupervisionBureau)) {
            childSupervisionBureau.forEach(children -> {
                childrenSet.add(children.getSupervisionId());
                addSupervisionIds(childrenSet, children);
            });
        }

    }

    private SupervisionBureau getParentSupervisionBureau(Long supervisionId) {
        List<SupervisionBureau> supervisionBureaus = queryAllSupervisionBureauDTO();
        Map<Long, SupervisionBureau> collect = supervisionBureaus.stream().collect(Collectors.toMap(SupervisionBureau::getSupervisionId, s -> s));
        for (SupervisionBureau bureau : collect.values()) {
            SupervisionBureau supervisionBureau = collect.get(bureau.getParentSupervisionId());
            if (ObjectUtil.isNotNull(supervisionBureau)) {
                List<SupervisionBureau> children = CollectionUtil.isNotEmpty(supervisionBureau.getChildSupervisionBureau()) ? supervisionBureau.getChildSupervisionBureau() : new ArrayList<>();
                children.add(bureau);
                supervisionBureau.setChildSupervisionBureau(children);
            }
        }
        return collect.get(supervisionId);
    }
/*    @Override
    public Set<Long> querySupervisionBureau(Long supervisionId, boolean flag) {
        return queryAllSupervisionBureauIds(flag).get(supervisionId);
    }*/

    /**
     * @param
     * @description: 获取所有监管所、监管局的数据
     * @return: java.util.List<com.fire.admin.dto.SupervisionBureauDTO>
     * @author: liuliu
     * @date: 2022-02-09 3:14
     */
    private List<SupervisionBureau> queryAllSupervisionBureauDTO() {
        List<SupervisionBureau> supervisionBureaus = baseMapper.selectList(Wrappers.<SupervisionBureau>query().select("supervision_id", "supervision_name", "parent_supervision_id", "create_time", "phone", "address_id").eq("is_delete", 0));
        if (CollectionUtil.isEmpty(supervisionBureaus)) {
            throw new BaseException("请联系管理员录入监管所、监管局信息");
        }
        return supervisionBureaus;
    }

    /**
     * @param flag ： 默认为flase。不包括自己的层级。true：包括自己的层级
     * @description:
     * @return: java.util.Map<java.lang.Long, java.util.Set < java.lang.Long>>
     * @author: liuliu
     * @date: 2022-02-09 3:13
     */
    private Map<Long, Set<Long>> queryAllSupervisionBureauIds(boolean flag) {
        Map<Long, Set<Long>> map = new HashMap<>(16);
        // addSupervisionId(map, queryAllSupervisionBureauTree(flag), flag);
        return map;
    }

    /**
     * @param
     * @description: 监管所树
     * @return: java.util.List<cn.hutool.core.lang.tree.Tree < java.lang.Object>>
     * @author: liuliu
     * @date: 2022-02-09 3:13
     */
    @Override
    public List<SupervisionBureau> queryAllSupervisionBureauTree() {
        List<SupervisionBureau> allSupervisionBureaus = queryAllSupervisionBureauDTO();
        allSupervisionBureaus.forEach(s -> {
            AddressInfo addressInfo = StrUtil.isNotEmpty(s.getAddressId()) ? AddressData.getAddressInfo(s.getAddressId()) : null;
            if (ObjectUtil.isNotEmpty(addressInfo)) {
                s.setProvince(addressInfo.getProvince());
                s.setCity(addressInfo.getCity());
                s.setArea(addressInfo.getArea());
            }
        });
        List<SupervisionBureau> parentSupervisionBureaus = allSupervisionBureaus.stream()
                .filter(s -> s.getParentSupervisionId() == 0L || ObjectUtil.isNull(s.getSupervisionId()))
                .peek(s -> s.setLevel(0))
                .collect(Collectors.toList());
        PreUtil.findSupervisionBureauChildren(parentSupervisionBureaus, allSupervisionBureaus);
        return parentSupervisionBureaus;
    }
 /*   @Override
    public List<Tree<Object>> queryAllSupervisionBureauTree(boolean flag) {
        List<TreeNode<Object>> treeNodeList = CollUtil.newArrayList();
        List<SupervisionBureau> supervisionBureaus = queryAllSupervisionBureauDTO();
        supervisionBureaus.forEach(d -> {
            treeNodeList.add(new TreeNode<>(d.getSupervisionId(), d.getParentSupervisionId(), d.getSupervisionName(), null));
        });
        Map<Long, SupervisionBureau> map = supervisionBureaus.stream().collect(Collectors.toMap(SupervisionBureau::getSupervisionId, d -> d));
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("supervisionId");
        treeNodeConfig.setParentIdKey("parentSupervisionId");
        treeNodeConfig.setNameKey("supervisionName");
        treeNodeConfig.setChildrenKey("childSupervisionBureau");
        treeNodeConfig.setDeep(4);
        return TreeUtil.build(treeNodeList, 0L, treeNodeConfig,
                (treeNode, tree) -> {
                    SupervisionBureau supervisionBureau = map.get(treeNode.getId());
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setName(treeNode.getName());
                    tree.putExtra("phone", supervisionBureau.getPhone());
                    tree.putExtra("addressId", supervisionBureau.getAddressId());
                    tree.putExtra("name", treeNode.getName());
                    AddressInfo addressInfo = AddressData.getAddressInfo(supervisionBureau.getAddressId());
                    if (ObjectUtil.isNotNull(addressInfo)) {
                        tree.putExtra("province", addressInfo.getProvince());
                        tree.putExtra("city", addressInfo.getCity());
                        tree.putExtra("area", addressInfo.getArea());
                    }
                });
    }*/

    /**
     * @description: 当前登录账户下的所有能看到的监管所
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.fire.admin.vo.SupervisionBureauVo>
     * @author: liuliu
     * @date: 2022-01-27 18:35
     */
    @Override
    public IPage<SupervisionBureauVo> querySupervisionBureauByUser(PageDTO pageDTO) {
        Page<SupervisionBureauVo> page = new Page<>();
        if (ObjectUtil.isNotEmpty(pageDTO)) {
            page.setCurrent(pageDTO.getCurrent());
            page.setSize(pageDTO.getSize());
        }

        Set<Long> supervisionIds = SecurityUtil.getUser().getSupervisionIds();
        // 如果该账户没有绑定监管所或者监管局得话就返回null
        if (CollectionUtil.isEmpty(supervisionIds)) {
            return null;
        }
        IPage<SupervisionBureauVo> supervisionBureauVoIPage = baseMapper.querySupervisionBureauByUserId(page, supervisionIds);
        List<SupervisionBureauVo> supervisionBureauVoList = supervisionBureauVoIPage.getRecords();
        if (CollectionUtil.isNotEmpty(supervisionBureauVoList)) {
            supervisionBureauVoList.forEach(su -> {
                AddressInfo addressInfo = AddressData.getAddressInfo(su.getAddressId());
                if (ObjectUtil.isNotNull(addressInfo)) {
                    su.setProvince(addressInfo.getProvince());
                    su.setCity(addressInfo.getCity());
                    su.setArea(addressInfo.getArea());
                }
            });
        }
        return supervisionBureauVoIPage;
    }

    /**
     * @param supervisionBureauDTO
     * @description: 监管所、监管局新增
     * @return: int
     * @author: liuliu
     * @date: 2022-02-10 12:17
     */
    @Override
    public boolean insertSupervisionBureau(SupervisionBureauDTO supervisionBureauDTO) {
        PreSecurityUser user = SecurityUtil.getUser();
        SupervisionBureau bureau = SupervisionBureau.builder()
                .supervisionName(supervisionBureauDTO.getName())
                .phone(supervisionBureauDTO.getPhone())
                .addressId(supervisionBureauDTO.getAddressId())
                .createTime(new Timestamp(System.currentTimeMillis()))
                .createAuthor(user.getUsername())
                .createAuthor(user.getUsername())
                .updateTime(new Timestamp(System.currentTimeMillis()))
                .parentSupervisionId(supervisionBureauDTO.getSupervisionId())
                .build();
        return this.save(bureau);
    }

    @Override
    public boolean updateSupervisionBureau(SupervisionBureauDTO supervisionBureauDTO) {
        PreSecurityUser user = SecurityUtil.getUser();
        SupervisionBureau bureau = SupervisionBureau.builder()
                .supervisionId(supervisionBureauDTO.getSupervisionId())
                .supervisionName(supervisionBureauDTO.getName())
                .phone(supervisionBureauDTO.getPhone())
                .addressId(supervisionBureauDTO.getAddressId())
                .createAuthor(user.getUsername())
                .updateTime(new Timestamp(System.currentTimeMillis()))
                .build();
        return this.updateById(bureau);
    }


    @Override
    public List<NotiDetaVo.NoticeSupervisionBureau> accountSupervisionBureau() {
        PreSecurityUser user = SecurityUtil.getUser();
        Set<Long> supervisionIds = user.getSupervisionIds();
        List<NotiDetaVo.NoticeSupervisionBureau> list = Lists.newArrayList();
        supervisionIds.forEach(id -> {
            NotiDetaVo.NoticeSupervisionBureau bureau = NotiDetaVo.NoticeSupervisionBureau.builder().supervisionId(id)
                    .supervisionName(SupervisionBureauData.getSupervisionBureauName(id))
                    .flag(false).build();
            if(bureau.getSupervisionName().contains("所")){
                list.add(bureau);
            }
        });
        return list;
    }

    /***
     *@description:
     * @param map 最终返回的监管所集合
     * @param treeNodes : 监管所树
     * @param flag : true ： 包括本级，false : 不包含本级
     *@return:
     *@author: liuliu
     *@date: 2022-01-27 17:31
     */
   /* private void addSupervisionId(Map<Long, Set<Long>> map, List<Tree<Object>> treeNodes, boolean flag) {
        // 首先获取所有的省监管局
        treeNodes.forEach(province -> {
            Set<Long> provinceSet = new HashSet<>();
            // 获取省份数据
            JSONObject provinceData = JSONUtil.parseObj(province);
            Long supervisionIdProvince = (Long) provinceData.get("supervisionId");
            if (flag) {
                provinceSet.add(supervisionIdProvince);
            }
            // 获取市监管局
            JSONArray cityArray = JSONUtil.parseArray(provinceData.get("childSupervisionBureau"));
            cityArray.forEach(city -> {
                Set<Long> citySet = new HashSet<>();
                JSONObject cityData = JSONUtil.parseObj(city);
                // 获取城市编号
                Long supervisionIdCity = (Long) cityData.get("supervisionId");
                if (flag) {
                    citySet.add(supervisionIdCity);
                }
                // 获取区监管局
                JSONArray areaArray = JSONUtil.parseArray(cityData.get("childSupervisionBureau"));
                areaArray.forEach(area -> {
                    Set<Long> areaSet = new HashSet<>();
                    // 获取区监管局编号
                    JSONObject areaData = JSONUtil.parseObj(area);
                    Long supervisionIdArea = (Long) areaData.get("supervisionId");
                    if (flag) {
                        areaSet.add(supervisionIdArea);
                    }
                    // 获取区域监管所
                    JSONArray townArray = JSONUtil.parseArray(areaData.get("childSupervisionBureau"));
                    townArray.forEach(town -> {
                        Set<Long> set = new HashSet<>();
                        // 获取监管所编号
                        JSONObject townData = JSONUtil.parseObj(town);
                        Long supervisionIdTown = (Long) townData.get("supervisionId");
                        provinceSet.add(supervisionIdTown);
                        citySet.add(supervisionIdTown);
                        areaSet.add(supervisionIdTown);
                        set.add(supervisionIdTown);
                        map.put(supervisionIdTown, set);
                    });
                    map.put(supervisionIdArea, areaSet);
                });
                map.put(supervisionIdCity, citySet);
            });
            map.put(supervisionIdProvince, provinceSet);
        });
    }*/


}
