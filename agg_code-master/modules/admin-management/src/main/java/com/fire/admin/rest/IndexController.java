package com.fire.admin.rest;

import com.fire.admin.service.ISysUserService;
import com.fire.dto.response.BaseRestResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @classname: IndexController
 * @description 主页模块
 * @author: liu liu
 * @create: 2020-08-26 15:45
 */
@RestController
@AllArgsConstructor
public class IndexController {


    private ISysUserService userService;

    /**
     * @Description: 登录
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/26 15:59
     */
    @RequestMapping(value = "/login")
    public BaseRestResponse login(String username, String password, HttpServletRequest request) {
        return new BaseRestResponse(userService.login(username, password, request));
    }

    /**
     * @Description: 暂时这样写了
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/26 15:59
     */
    @RequestMapping("/info")
    public BaseRestResponse info() {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("admin");
        map.put("roles", list);
        map.put("avatar", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561394014552&di=17b6c1233048e5276f48309b306c7699&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201804%2F29%2F20180429210111_gtsnf.jpg");
        map.put("name", "Super Admin");
        return new BaseRestResponse(map);
    }

    /**
     * @Description: 使用jwt前后分离 只需要前端清除tsuccessen即可 暂时返回success
     * @Param:
     * @return:
     * @Author: liu liu
     * @date: 2020/8/26 15:59
     */
    @RequestMapping("/logout")
    public String logout() {
        return "success";
    }
}
