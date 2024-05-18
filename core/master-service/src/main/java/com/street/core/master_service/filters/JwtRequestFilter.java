package com.street.core.master_service.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.street.core.master_service.entity.UserEntity;
import com.street.core.master_service.enums.JwtTokenUser;
import com.street.core.master_service.enums.LoginUserUtils;
import com.street.core.master_service.response.AuthResponse;
import com.street.core.master_service.service.AuthService;
import com.street.core.master_service.utils.JwtUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.*;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
    static final private String AUTH_METHOD = "Bearer";

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoginUserUtils loginUserUtils;

    public String getToken(HttpServletRequest httpRequest) {
        String apiKey = null;

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null) {
            authHeader = authHeader.trim();
            if (authHeader.startsWith(AUTH_METHOD)) {
                apiKey = authHeader.substring(AUTH_METHOD.length()).trim();
            }
        }

        return apiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
                String token = null;
                Long userId = null;
                String ipAddress = request.getRemoteAddr();
                String url = request.getRequestURI();
                String method = request.getMethod();


                LOGGER.info("\n\n\n\n\n\n\n{}\n\n\n\n\n\n\n",request.getRequestURI());

                if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
                    ipAddress = "127.0.0.1";
                }
        
                if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
        
                    token = getToken((HttpServletRequest) request);
                    if (token != null) {
                        try {
                            if (jwtUtil.isTokenExpired(token)) {
                                return;
                            }
                            userId = jwtUtil.extractUserId(token);
                        } catch (ExpiredJwtException ex) {
                            LOGGER.info("Token Expired 71 {}", ex.getMessage());
                            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            ((HttpServletResponse) response).setContentType("application/json");
                            String jsonResponse = "{\"status\": 401, \"message\": \""+ex.getMessage()+"\", \"data\": null}";
                            response.getWriter().write(jsonResponse);
                            return;
                        }
                    }
        
                    if (userId != null) {
                        AuthResponse userDetails = authService.loadUserByUserId(userId);
        
                        
                        Long id = jwtUtil.extractUserId(token);
                        UserEntity user = authService.findById(id);
        
                        List<String> path = userDetails.getPermissions();
                        user.getRole().setPermissions(null);
        
                        request.setAttribute("user", user);
                        request.setAttribute("token", token);
        
                        JwtTokenUser tokenUser = JwtTokenUser.builder().id(user.getId()).roleId(user.getRole().getId())
                                .jwtToken(token).build();
        
                        loginUserUtils.setTokenUser(tokenUser);
        
                    }
        
                }
        
                chain.doFilter(request, response);
    }

}
