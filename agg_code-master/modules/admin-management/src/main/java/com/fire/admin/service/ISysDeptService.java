package com.fire.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fire.admin.dto.DeptDTO;
import com.fire.admin.vo.DeptTreeVo;
import com.fire.dto.system.SysDept;

import java.io.Serializable;
import java.util.List;


/**
 * @classname: ISysDeptService
 * @description 部门管理 服务类
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * @Description: 查询部门信息
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:34
     */
    List<SysDept> selectDeptList();

    /**
     * @Description: 更新部门
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    boolean updateDeptById(DeptDTO entity);

    /**
     * @Description: 删除部门
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * @Description: 根据部门id查询部门名称
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    String selectDeptNameByDeptId(Long deptId);

    /**
     * @Description: 根据部门名称查询
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    List<SysDept> selectDeptListBydeptName(String deptName);

    /**
     * @Description: 通过此部门id查询于此相关的部门ids
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    List<Long> selectDeptIds(Long deptId);

    /**
     * @Description: 获取部门树
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/31 15:35
     */
    List<DeptTreeVo> getDeptTree();


}
