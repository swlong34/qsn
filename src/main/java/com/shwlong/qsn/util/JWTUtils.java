package com.shwlong.qsn.util;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@ConfigurationProperties(prefix = "qsn.jwt")
@Component
@Data
public class JWTUtils {

    private String secret;
    private long expire;
    private String header;

    /**
     * 生成jwt token
     */
    public String generateToken(int userid, String username) {
        //过期时间
        Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);

        return Jwts.builder()
                // header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // payload
                .claim(Constant.USER_NAME, username)
                .claim(Constant.USER_ID, userid)
                .setSubject("shwlong.qsn.com")
                .setExpiration(expireDate)
                // signature
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 解析token 获取token载荷中的信息
     *
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

}
