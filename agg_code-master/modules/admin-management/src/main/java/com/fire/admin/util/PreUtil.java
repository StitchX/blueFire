package com.fire.admin.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fire.admin.entity.AddressInfo;
import com.fire.admin.entity.CategoryInfo;
import com.fire.admin.entity.SupervisionBureau;
import com.fire.admin.task.AddressData;
import com.fire.admin.vo.DeptTreeVo;
import com.fire.admin.vo.MenuMetaVo;
import com.fire.admin.vo.MenuVo;
import com.fire.dto.system.SysDept;
import com.fire.dto.system.SysMenu;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * @author: liuliu
 * @ClassName: PreUtil
 * @Description: 系统用户工具类
 * @date: 2021-05-13 18:18
 */
@UtilityClass
public class PreUtil {


    public List<MenuVo> buildMenus(List<SysMenu> sysMenuList) {
        List<MenuVo> list = new LinkedList<>();
        sysMenuList.forEach(sysMenu -> {
            if (sysMenu != null) {
                List<SysMenu> menuDTOList = sysMenu.getChildren();
                MenuVo menuVo = new MenuVo();
                menuVo.setName(sysMenu.getName());
                menuVo.setPath(sysMenu.getPath());
                //   如果不是外链
                if (sysMenu.getIsFrame()) {
                    if (sysMenu.getParentId().equals(0L)) {
                        // 一级目录需要加斜杠，不然访问 会跳转404页面
                        menuVo.setPath("/".concat(sysMenu.getPath()));
                        menuVo.setComponent(StrUtil.isEmpty(sysMenu.getComponent()) ? "Layout" : sysMenu.getComponent());
                    } else if (!StrUtil.isEmpty(sysMenu.getComponent())) {
                        menuVo.setComponent(sysMenu.getComponent());
                    }
                }

                menuVo.setMeta(new MenuMetaVo(sysMenu.getName(), sysMenu.getIcon()));
                if (menuDTOList != null && menuDTOList.size() != 0 && sysMenu.getType() == 0) {
                    menuVo.setChildren(buildMenus(menuDTOList));
                    //   处理是一级菜单并且没有子菜单的情况
                } else if (sysMenu.getParentId().equals(0L)) {
                    menuVo.setAlwaysShow(false);
                    menuVo.setRedirect("noredirect");
                    MenuVo menuVo1 = new MenuVo();
                    menuVo1.setMeta(menuVo.getMeta());
                    //   非外链
                    if (sysMenu.getIsFrame()) {
                        menuVo1.setPath("index");
                        menuVo1.setName(menuVo.getName());
                        menuVo1.setComponent(menuVo.getComponent());
                    } else {
                        menuVo1.setPath(sysMenu.getPath());
                    }
                    menuVo.setName(null);
                    menuVo.setMeta(null);
                    menuVo.setComponent("Layout");
                    List<MenuVo> list1 = new ArrayList<>();
                    list1.add(menuVo1);
                    menuVo.setChildren(list1);
                }
                list.add(menuVo);
            }
        });
        list.forEach(li -> {
            System.out.println("li" + li.toString());
        });
        return list;


    }


    /**
     * @Description: 遍历菜单
     * @Param: [menuList, menus, menuType]
     * @return: void
     * @Author: liuliu
     * @Date: 2021/5/13 18:24
     */
    public void findChildren(List<SysMenu> menuList, List<SysMenu> menus, Integer menuType) {
        for (SysMenu sysMenu : menuList) {
            List<SysMenu> children = new ArrayList<>();
            for (SysMenu menu : menus) {
                if (menuType == 1 && menu.getType() == 2) {
                    // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
                    continue;
                }
                if (ObjectUtil.isNotEmpty(sysMenu.getMenuId()) && sysMenu.getMenuId().equals(menu.getParentId())) {
                    menu.setParentName(sysMenu.getName());
                    menu.setLevel(sysMenu.getLevel() + 1);
                    if (exists(children, menu)) {
                        children.add(menu);
                    }
                }
            }
            sysMenu.setChildren(children);
            children.sort((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
            findChildren(children, menus, menuType);
        }
    }


    /**
     * @Description: 构建行业分类树
     * @Param: [sysDepts, depts]
     * @return: void
     * @Author: liuliu
     * @Date: 2021/5/13 18:25
     */
    public void findChildren(List<CategoryInfo> categoryInfos, List<CategoryInfo> categoryAll) {

        for (CategoryInfo sysCategory : categoryInfos) {
            List<CategoryInfo> children = new ArrayList<>();
            for (CategoryInfo parentCategory : categoryAll) {
                if (ObjectUtil.isNotEmpty(sysCategory.getCategoryId()) && sysCategory.getCategoryId().equals(parentCategory.getParentCategoryId())) {
                    parentCategory.setParentCategoryName(sysCategory.getCategoryName());
                    parentCategory.setLevel(sysCategory.getLevel() + 1);
                    children.add(parentCategory);
                }
            }
            //  对子部门排序
            children.sort((category1, category2) -> Integer.compare(category1.getSort(), category2.getSort()));
            sysCategory.setChildenCategoryInfo(children);
            findChildren(children, categoryAll);
        }
    }


    public void findSupervisionBureauChildren(List<SupervisionBureau> parentSupervisionBureaus, List<SupervisionBureau> allSupervisionBureaus) {
        for (SupervisionBureau parentSupervisionBureau : parentSupervisionBureaus) {
            List<SupervisionBureau> children = new ArrayList<>();
            for (SupervisionBureau allSupervisionBureau : allSupervisionBureaus) {
                if (ObjectUtil.isNotEmpty(parentSupervisionBureau.getSupervisionId()) && parentSupervisionBureau.getSupervisionId().equals(allSupervisionBureau.getParentSupervisionId())) {
                    allSupervisionBureau.setParentCategoryName(parentSupervisionBureau.getSupervisionName());
                    allSupervisionBureau.setLevel(parentSupervisionBureau.getLevel() + 1);

                    children.add(allSupervisionBureau);
                }
            }
            parentSupervisionBureau.setChildSupervisionBureau(children);
            findSupervisionBureauChildren(children, allSupervisionBureaus);
        }
    }

    /**
     * @Description: 判断菜单是否存在
     * @Param: [sysMenus, sysMenu]
     * @return: boolean
     * @Author: liuliu
     * @Date: 2021/5/13 18:25
     */
    public boolean exists(List<SysMenu> sysMenus, SysMenu sysMenu) {
        boolean exist = false;
        for (SysMenu menu : sysMenus) {
            if (menu.getMenuId().equals(sysMenu.getMenuId())) {
                exist = true;
            }
        }
        return !exist;
    }


    /**
     * @Description: 生成BCryptPasswordEncoder密码
     * @Param: [rawPass]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/13 18:25
     */
    public String encode(String rawPass) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(rawPass);
    }


    /**
     * @Description: 校验密码
     * @Param: [newPass, passwordEncoderOldPass]
     * @return: boolean
     * @Author: liuliu
     * @Date: 2021/5/13 18:25
     */
    public boolean validatePass(String newPass, String passwordEncoderOldPass) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(newPass, passwordEncoderOldPass);
    }


    /**
     * @Description: 不重复的验证码
     * @Param: [i]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/13 18:26
     */
    public String codeGen(int i) {
        char[] codeSequence = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'L', 'K', 'J', 'H', 'G', 'F', 'D', 'S', 'A', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        while (true) {
            // 随机产生一个下标，通过下标取出字符数组中对应的字符
            char c = codeSequence[random.nextInt(codeSequence.length)];
            // 假设取出来的字符在动态字符中不存在，代表没有重复的
            if (stringBuilder.indexOf(c + "") == -1) {
                stringBuilder.append(c);
                count++;
                //控制随机生成的个数
                if (count == i) {
                    break;
                }
            }
        }
        return stringBuilder.toString();
    }
}
