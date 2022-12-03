package com.blue.crm.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.blue.crm.dto.LoginUser;
import com.blue.crm.entity.BD;
import com.blue.crm.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author: liuliu
 * @ClassName: JwtUtil
 * @Description: JWT工具类
 * @date: 2021-05-13 18:08
 */
@Slf4j
@Component
public class JwtUtil {

    /**
     * 用户名称
     */
    private static final String USERNAME = Claims.SUBJECT;

    private static final String USERID = "userid";

    private static final String BD_ID = "bdId";

    private static final String DEPT_ID = "deptId";

    /**
     * 创建时间
     */
    private static final String CREATED = "created";
    /**
     * 权限列表
     */
    private static final String AUTHORITIES = "authorities";
    /**
     * 密钥
     */
    private static final String SECRET = "abcdefgh";
    /**
     * 有效期24小时
     */
    private static final long EXPIRE_TIME = 864000000;

    /**
     * @Description: 从数据声明生成令牌
     * @Param: [claims] 数据声明
     * @return: java.lang.String 令牌
     * @Author: liuliu
     * @Date: 2021/5/11 18:07
     */
    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }


    /**
     * @Description: 生成令牌
     * @Param: [userDetail]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/13 18:18
     */
    public static String generateToken(LoginUser userDetail) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(USERID, userDetail.getUserId());
        claims.put(USERNAME, userDetail.getUsername());
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, userDetail.getAuthorities());
        claims.put(BD_ID, userDetail.getBdId());
        claims.put(DEPT_ID, userDetail.getDeptId());
        return generateToken(claims);
    }

    public static String getUsernameFromToken(String token) {
        String username;
        if (StrUtil.isNotEmpty(token)) {
            token = token.substring("Bearer".length());
        }
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        LoginUser user = (LoginUser) userDetails;
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername())&& !isTokenExpired(token));
    }


    /**
     * @Description: 从令牌中获取数据声明
     * @Param: [token]  令牌
     * @return: io.jsonwebtoken.Claims 数据声明
     * @Author: liuliu
     * @Date: 2021/5/13 18:19
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * @Description: 判断令牌是否过期
     * @Param: token 令牌
     * @return: java.lang.Boolean 是否过期
     * @Author: liuliu
     * @Date: 2021/5/13 18:20
     */
    private static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
