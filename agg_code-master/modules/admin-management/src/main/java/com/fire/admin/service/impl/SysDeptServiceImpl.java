package com.fire.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fire.admin.dto.DeptDTO;
import com.fire.admin.mapper.SysDeptMapper;
import com.fire.admin.service.ISysDeptService;
import com.fire.admin.util.PreUtil;
import com.fire.admin.vo.DeptTreeVo;
import com.fire.dto.system.SysDept;
import com.fire.utils.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @classname: SysDeptServiceImpl
 * @description 部门管理 服务实现类
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@Service
@Slf4j
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    /**
     * @Description: 查询部门信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:34
     */
    @Override
    public List<SysDept> selectDeptList() {
        List<SysDept> depts = baseMapper.selectList(Wrappers.<SysDept>lambdaQuery().select(SysDept::getDeptId, SysDept::getName, SysDept::getParentId, SysDept::getSort, SysDept::getCreateTime));
        List<SysDept> sysDepts = depts.stream().filter(sysDept -> sysDept.getParentId().equals(4L) || ObjectUtil.isNull(sysDept.getParentId())).peek(sysDept -> sysDept.setLevel(0)).collect(Collectors.toList());
        //PreUtil.findChildren(sysDepts, depts);
        //  对一级部门排序
        sysDepts.sort((dept1, dept2) -> Integer.compare(dept1.getSort(), dept2.getSort()));
        return sysDepts;
    }


    /**
     * @Description: 更新部门
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    @Override
    
    public boolean updateDeptById(DeptDTO entity) {
        SysDept sysDept = new SysDept();
        BeanUtil.copyProperties(entity, sysDept);
        sysDept.setUpdateTime(DateUtils.strformatDatetime(LocalDateTime.now()));
        return this.updateById(sysDept);
    }


    /**
     * @Description: 删除部门
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        // 部门层级删除
        List<Long> idList = this.list(Wrappers.<SysDept>query().lambda().eq(SysDept::getParentId, id)).stream().map(SysDept::getDeptId).collect(Collectors.toList());
        // 删除自己
        idList.add((Long) id);
        log.info("删除部门编号为：【{}】", JSONUtil.parseArray(idList));
        return super.removeByIds(idList);
    }

    /**
     * @Description: 根据部门id查询部门名称
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    @Override
    public String selectDeptNameByDeptId(Long deptId) {
        return baseMapper.selectOne(Wrappers.<SysDept>query().lambda().select(SysDept::getName).eq(SysDept::getDeptId, deptId)).getName();
    }

    /**
     * @Description: 根据部门名称查询
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    @Override
    public List<SysDept> selectDeptListBydeptName(String deptName) {
        return null;
    }

    /**
     * @Description: 通过此部门id查询于此相关的部门ids
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    @Override
    public List<Long> selectDeptIds(Long deptId) {
        SysDept department = this.getDepartment(deptId);
        List<Long> deptIdList = new ArrayList<Long>();
        if (department != null) {
            addDeptIdList(deptIdList, department);
        }
        return deptIdList;
    }

    /**
     * @Description: 获取部门树
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    @Override
    public List<DeptTreeVo> getDeptTree() {
        List<SysDept> depts = baseMapper.selectList(Wrappers.<SysDept>query().select("dept_id", "name", "parent_id", "sort", "create_time"));
        List<DeptTreeVo> collect = depts.stream().filter(sysDept -> sysDept.getParentId().equals(4L) || ObjectUtil.isNull(sysDept.getParentId())).map(sysDept -> {
            DeptTreeVo deptTreeVo = new DeptTreeVo();
            deptTreeVo.setId(sysDept.getDeptId());
            deptTreeVo.setLabel(sysDept.getName());
            return deptTreeVo;
        }).collect(Collectors.toList());

        return collect;
    }


    /**
     * @Description: 根据部门ID获取该部门及其下属部门树
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:37
     */
    
    private SysDept getDepartment(Long deptId) {
        List<SysDept> departments = baseMapper.selectList(Wrappers.<SysDept>query().select("dept_id", "name", "parent_id", "sort", "create_time"));
        Map<Long, SysDept> map = departments.stream().collect(Collectors.toMap(SysDept::getDeptId, department -> department));

        for (SysDept dept : map.values()) {
            SysDept parent = map.get(dept.getParentId());
            if (parent != null) {
                List<SysDept> children = parent.getChildren() == null ? new ArrayList<>() : parent.getChildren();
                children.add(dept);
                parent.setChildren(children);
            }
        }
        return map.get(deptId);
    }

    private void addDeptIdList(List<Long> deptIdList, SysDept department) {
        List<SysDept> children = department.getChildren();
        if (children != null) {
            for (SysDept d : children) {
                deptIdList.add(d.getDeptId());
                addDeptIdList(deptIdList, d);
            }
        }
    }


}
