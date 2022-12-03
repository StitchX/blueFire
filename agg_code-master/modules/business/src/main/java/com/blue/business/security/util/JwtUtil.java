package com.blue.business.security.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

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
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    /**
     * 用户名称
     */
    private static final String USERNAME = Claims.SUBJECT;

    private static final String MERCHANT_ID = "merchantId";
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

    @Value("${jwt.header:Authorization}")
    private String tokenHeader;

    @Value("${jwt.tokenHead:Bearer}")
    private String authTokenStart;

    /**
     * @Description: 生成令牌
     * @Param: [userDetail]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/13 18:18
     */
    public static String generateToken(PreSecurityUser userDetail) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(USERNAME, userDetail.getUsername());
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, userDetail.getAuthorities());
        claims.put(MERCHANT_ID, userDetail.getMerchantId());
        return generateToken(claims);
    }


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
     * @Description: 从令牌中获取用户名
     * @Param: [token]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/13 18:18
     */
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


    /**
     * @Description: 根据请求令牌获取登录认证信息
     * @Param: [request]
     * @return: com.fire.admin.util.PreSecurityUser
     * @Author: liuliu
     * @Date: 2021/5/13 18:19
     */
    public PreSecurityUser getUserFromToken(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);

        if (StrUtil.isNotEmpty(token)) {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return null;
            }
            String username = claims.getSubject();
            if (username == null) {
                return null;
            }
            if (isTokenExpired(token)) {
                return null;
            }
            // 解析对应的权限以及用户id
            Object authors = claims.get(AUTHORITIES);
            Long merchantId = ObjectUtil.isNotEmpty(claims.get(MERCHANT_ID)) ? Long.parseLong(claims.get(MERCHANT_ID).toString()) : null;

            Set<String> perms = new HashSet<>();
            if (authors instanceof List) {
                for (Object object : (List) authors) {
                    perms.add(((Map) object).get("authority").toString());
                }
            }
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(perms.toArray(new String[0]));
            if (validateToken(token, username)) {
                // 未把密码放到jwt
                return new PreSecurityUser(username, merchantId, authorities);
            }
        }
        return null;
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
     * @Description: 验证令牌
     * @Param: token
     * @return: java.lang.Boolean
     * @Author: liuliu
     * @Date: 2021/5/13 18:19
     */
    public static Boolean validateToken(String token, String username) {
        String userName = getUsernameFromToken(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }


    /**
     * @Description: 刷新令牌
     * @Param: [token]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/13 18:20
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
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


    /**
     * @Description: 获取请求token
     * @Param: [request]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/13 18:20
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (StrUtil.isNotEmpty(token)) {
            token = token.substring(authTokenStart.length());
        }
        return token;
    }


}
