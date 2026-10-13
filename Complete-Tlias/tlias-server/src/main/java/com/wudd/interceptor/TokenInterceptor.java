package com.wudd.interceptor;

import com.wudd.util.JwtUtils;
import com.wudd.util.ThreadLocalUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor { // 实现接口

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 先简单实现，那个太严谨了严谨到有一点看不懂了，所以先简单实现，再深入研究
        String token = request.getHeader("token");

        // 1. 检验是否登录
        if (token == null || token.isEmpty()) {
            log.info("用户未登录");
            writeUnauthorizedResponse(response,"请先登录");
            return false;
        }

        // 2. 校验有效性（由于需要记录登录状态所以我们先解析）
        //if(!JwtUtils.validateToken(token)) {
        //    writeUnauthorizedResponse(response,"登录过期，请重新登录");
        //    return false;
        //}
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            // token可能被串改或者token过期了
            log.info("token校验失败，token过期或者被篡改");
            writeUnauthorizedResponse(response,"请重新登录！");
            return false;
        }

        // 3.将需要的信息存放到ThreadLocal中，存username就可以知道登录状态了(username存放在subject里面)
        log.info("token校验通过");
        ThreadLocalUtils.setName(claims.getSubject());

        return true;
    }


    /**
     * 严谨：统一响应写入，确保编码、Content-Type、状态码完全正确
     * 返回false后Spring MVC不再处理响应，必须手动写回
     */
    // 参数是有Tomcat服务器的servlet传递过来的
    // 如果token校验未通过，就使用这个方法设置响应头响应体
    private void writeUnauthorizedResponse(HttpServletResponse response, String message) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置响应状态码
        response.setContentType("application/json;charset=utf-8"); // ???
        response.setHeader("Cache-Control", "no-store"); // ???

        // 写入响应体的数据
        Map<String, Object> body = new HashMap<>();
        //Map<String, Object> body = new LinkedHashMap<>(); // ???

        body.put("code",401); // 未登录响应状态码
        body.put("message",message); // 自定义提示信息


        // ？？？
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(body);
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
