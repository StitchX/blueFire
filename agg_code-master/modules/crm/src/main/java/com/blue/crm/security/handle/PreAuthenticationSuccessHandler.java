package com.blue.crm.security.handle;

import com.alibaba.fastjson.JSON;
import com.blue.crm.dto.LoginUser;
import com.blue.crm.util.JwtUtil;
import com.fire.dto.response.BaseResponse;
import com.fire.dto.response.BaseRestResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
public class PreAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //生成token
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUser) {
            LoginUser userDetail = (LoginUser) authentication.getPrincipal();
            //存储认证信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //生成token
            String token = JwtUtil.generateToken(userDetail);

            HashMap<String,Object> map = new HashMap<>();
            map.put("token", token);
            Object result = new BaseRestResponse<>(map);
            String json =  JSON.toJSONString(result);
            httpServletResponse.setContentType("text/json;charset=utf-8");
            httpServletResponse.getWriter().write(json);
        }
    }
}
