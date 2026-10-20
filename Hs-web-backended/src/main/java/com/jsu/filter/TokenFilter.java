package com.jsu.filter;


import com.jsu.util.CurrentHolder;
import com.jsu.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 令牌校验过滤器
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. 获取请求的url地址
        String uri = request.getRequestURI();

        //2. 判断是否是登录请求, 精确匹配 /login 路径则放行
        if ("/login".equals(uri)) {
            log.info("登录请求, 放行");
            filterChain.doFilter(request, response);
            return;
        }

        //2.1 放行 CORS 预检请求（OPTIONS 请求不带 token 头）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.info("OPTIONS 预检请求, 放行");
            filterChain.doFilter(request, response);
            return;
        }

        //2.2 放行图片等静态资源（无需 token）
        if (uri.startsWith("/images/")) {
            log.info("静态资源请求, 放行");
            filterChain.doFilter(request, response);
            return;
        }

        //3. 获取请求中的token
        String token = request.getHeader("token");

        //4. 判断token是否为空, 如果为空, 响应401状态码
        if (token == null || token.isEmpty()) {
            log.info("token为空, 响应401状态码");
            response.setStatus(401);
            return;
        }

        //5. 如果token不为空, 调用JWtUtils工具类的方法解析token, 如果解析失败, 响应401状态码
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId);
            // 从JWT中提取角色信息并设置到CurrentHolder
            Object roleObj = claims.get("role");
            if (roleObj != null) {
                CurrentHolder.setCurrentRole(Integer.valueOf(roleObj.toString()));
            }
            // 从JWT中提取姓名信息
            Object nameObj = claims.get("name");
            if (nameObj != null) {
                CurrentHolder.setCurrentName(nameObj.toString());
            }
            log.info("token解析成功, 放行, id={}, role={}, name={}", empId, CurrentHolder.getCurrentRole(), CurrentHolder.getCurrentName());
        } catch (Exception e) {
            log.info("token解析失败, 响应401状态码");
            response.setStatus(401);
            return;
        }

        //6. 放行
        try {
            filterChain.doFilter(request, response);
        } finally {
            //7. 清空当前线程绑定的id
            CurrentHolder.remove();
        }
    }
}