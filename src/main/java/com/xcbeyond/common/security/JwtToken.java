package com.xcbeyond.common.security;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;
/**
 * JWT(JSON Web Token) 工具类
 * @Auther: xcbeyond
 * @Date: 2019/5/24 13:45
 */
public class JwtToken {
    private static Clock clock = DefaultClock.INSTANCE;

    //密钥
    private String secret;

    //失效时间，秒
    private int expiration = 3600;

    public JwtToken(String secret) {
        this.secret = secret;
    }

    public JwtToken(String secret, int expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    /**
     * 生成token
     * @param payload
     * @return
     */
    public String generateToken(String payload) {
        JSONObject json = JSONObject.parseObject(payload);
        Map<String, Object> claims = json.getInnerMap();
        return doGenerateToken(claims);
    }

    /**
     * 校验token
     * @param token token字符串
     * @param payload
     * @return
     */
    public Boolean validateToken(String token, String payload) {
        boolean result;
        try {
            JSONObject jsonObject = JSONObject.parseObject(payload);
            String username = jsonObject.getString("username");

            String tokenUsername = getUsernameFromToken(token);
//            Date created = getIssuedAtDateFromToken(token);
            result = username.equals(tokenUsername)
                    && !isTokenExpired(token);
//                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())

        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     * 是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    private String doGenerateToken(Map<String, Object> claims) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    private String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}
