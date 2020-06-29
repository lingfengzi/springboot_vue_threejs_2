package com.example.springboot_vue.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.sf.json.JSONObject;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final long EXPIRATION_TIME = (long)(168*3600*1000); // 有效时间：7天 24h*7=168h，转换为时间戳单位
    private static final String SECRET = "secret";//  密钥

    //解析
    public static JSONObject parseJWT(String jsonWebToken) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(jsonWebToken).getBody();

            //System.out.println(claims);

            JSONObject json=new JSONObject();
            //json.put("userId",claims.get("userId"));
            //json.put("password",claims.get("password"));
            json.put("nbf",claims.getNotBefore().getTime());
            json.put("exp",claims.getExpiration().getTime());
            return json;
        }catch (Exception e){
            return  null;
        }
    }

    //生成
    public static String createJWT(String userId, String password) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //当前时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("type", "JWT")
                .claim("userId", userId)
                .claim("password", password)
                .signWith(signatureAlgorithm, signingKey);

        //添加Token过期时间
        long expMillis = nowMillis + EXPIRATION_TIME;
        Date exp = new Date(expMillis);
        jwtBuilder.setExpiration(exp).setNotBefore(now);

        //返回生成的JWT字符串
        return jwtBuilder.compact();
    }


    public static void main(String[] args){
        String token = JwtUtil.createJWT("10001", "123456");
        System.out.println(token);

        JSONObject json=JwtUtil.parseJWT(token);
        System.out.println(json.toString());
    }

}
