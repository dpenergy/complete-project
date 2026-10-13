package com.wudd.util;

import com.wudd.pojo.Admi;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    // --------------- 定义生成令牌的密钥 ---------------
    private static final String SECRET_STRING = "jflaemdnbupquirerhhfjhaj878ewqj34j1434j"; // 米哟啊原型
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes()); // 生成密钥key
    private static final long EXPIRATION_MS = 24* 60 * 60 * 1000; // 令牌有效期一天




    // --------------- 生成Jwt令牌 ---------------
    // 头，有效载荷（自定义备注信息），加密算法签名（头+有效载荷+密钥计算出）
    public static String generateToken(String username) {
        // 信息：发布者，接收者，发布时间，有效期，自定义的备注
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);
        Map<String,Object> map = new HashMap<>();

        JwtBuilder builder = Jwts.builder()
                .issuer("tlias-server") // 发布者
                .subject(username) // 令牌的发布对象
                .issuedAt(now)
                .expiration(expiration)
                .signWith(SECRET_KEY);

        // 如果有其它的自定义信息，就可以再添加claims到builder里面
        return builder.compact();
    }




    // --------------- 解析Jwt令牌 ---------------
    // 可能抛出异常，用try-catch处理
    public static Claims parseToken(String token) {
        try {
            return  Jwts.parser() // 生成解析器模板
                    .verifyWith(SECRET_KEY) // 填充解析器密钥
                    .build() // 构建解析器
                    .parseClaimsJws(token) // 传入token进行解析
                    .getPayload(); // 获取有效载荷返回
        } catch (JwtException | IllegalArgumentException e) {
            // 过期、篡改、格式错误等全部归为null，这都是token有误
            // 其它非token造成的错误还是会抛出，然后被全局异常处理器捕获，响应给前端
            return null;
        }

    }


    // --------------- 校验Jwt令牌 ---------------
    public static boolean validateToken(String token) {
        return  parseToken(token) != null;
    }
}
