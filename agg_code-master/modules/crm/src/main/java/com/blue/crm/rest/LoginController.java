package com.blue.crm.rest;

import com.blue.crm.entity.BD;
import com.blue.crm.service.BDService;
import com.fire.dto.response.BaseRestResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @classname: LoginController
 * @description 登陆
 * @author: Cohen
 * @create: 2020-08-26 15:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private BDService bdService;

    @PostMapping("/info")
    public BaseRestResponse info() {
        BD bd = bdService.getLoginBDInfo();
        return new BaseRestResponse(bd);
    }
}
