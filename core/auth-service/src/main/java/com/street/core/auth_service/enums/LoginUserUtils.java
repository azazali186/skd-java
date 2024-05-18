package com.street.core.auth_service.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class LoginUserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserUtils.class);

    protected String token;

    @Builder.Default
    protected String tokenRequestAttribute = "Authorization";

    public static LoginUserUtils loadHeaderData(HttpServletRequest request) {
        return builder()
                .tokenRequestAttribute("Authorization")
                .build();
    }

    @SuppressWarnings("null")
    public void setTokenUser(JwtTokenUser jwtToken) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        request.setAttribute(getTokenRequestAttribute(), jwtToken);
    }

    @SuppressWarnings("null")
    public JwtTokenUser getTokenUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Object jwtToken = request.getAttribute(getTokenRequestAttribute());
        if (jwtToken == null) {
            // 当前操作不是登陆用户操作 = The current operation is not a logged-in user operation
            throw new SecurityException("当前操作不是登陆用户操作");
        }
        JwtTokenUser jToken = (JwtTokenUser) jwtToken;
        return jToken;
    }

    
}
