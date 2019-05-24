package com.xcbeyond.common.security;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

/**
 * JwtToken测试用例
 * @Auther: xcbeyond
 * @Date: 2019/5/23 15:03
 */
public class JwtTokenTest {
    private JwtToken jwtToken;
    private String payload;

    @Before
    public void init() {
        //UUID随机值作为密钥
        jwtToken = new JwtToken(UUID.randomUUID().toString());
        JSONObject json = new JSONObject();
        json.put("username", "xcbeyond");
        payload = json.toJSONString();
    }

    /**
     *生成token
     */
    @Test
    public void createToken() {
        String token = jwtToken.generateToken(payload);
        System.out.println(token);
    }

    /**
     * 校验token
     */
    @Test
    public void validateToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NTg2MDE1OTUsImlhdCI6MTU1ODYwMTUzNSwidXNlcm5hbWUiOiJ4dWNoYW8ifQ.H7GA1Gi3uPdAyDGxoW-KAOBDVIpKkBiyBoO3c0_PHDuGtlY48Ogd09I0yWSvNs0s3TRWdQvAJ3Uy5UBYQXMQig";
        boolean result = jwtToken.validateToken(token, payload);
        Assert.assertTrue(result);
    }
}