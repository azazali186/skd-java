package com.street.core.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import com.street.core.entity.UserEntity;
import com.street.core.enums.JwtTokenUser;
import com.street.core.enums.LoginUserUtils;
import com.street.core.response.ApiResponse;
import com.street.core.service.AuthService;
import com.street.core.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;

@Component
public class JwtRequestFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
    static final private String AUTH_METHOD = "Bearer";

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoginUserUtils loginUserUtils;

    @SuppressWarnings("unused")
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = null;
        String username = null;
        String authHeader = null;
        String ipAddress = request.getRemoteAddr();
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
                    username = jwtUtil.extractUsername(token);
                } catch (ExpiredJwtException ex) {
                    LOGGER.info("Token Expired 71 {}", ex.getMessage());
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    ((HttpServletResponse) response).setContentType("application/json");
                    String jsonResponse = "{\"status\": 401, \"message\": \""+ex.getMessage()+"\", \"data\": null}";
                    response.getWriter().write(jsonResponse);
                    return;
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = authService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                Long id = jwtUtil.extractUserId(token);
                UserEntity user = authService.findById(id);

                Object path = userDetails.getAuthorities();
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

}
