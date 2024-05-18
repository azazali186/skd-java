package com.street.core.master_service.enums;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.street.core.master_service.utils.AESUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenUser {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUser.class);
    Long id;
    String username;
    String role;
    Long roleId;
    String tokenId;
    String jwtToken;

    public Map<String, Object> backMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("username", this.username);
        map.put("role", this.role);
        map.put("roleId", this.roleId);
        map.put("tokenId", this.tokenId);
        map.put("jwtToken", this.jwtToken);
        return map;
    }

    public static JwtTokenUser simpleGenerateUser(String username, Integer userId, String userType, String role, Long roleId) {
        JwtTokenUser jwtToken = builder().username(username).build();
        jwtToken.encodeID(String.valueOf(userId));
        jwtToken.setRole(role);
        jwtToken.setRoleId(roleId);
        return jwtToken;
    }

    @SuppressWarnings("deprecation")
    public String decodeID() {
        if (StringUtils.isEmpty(this.id)) {
            return "";
        }
        try {
            return AESUtil.decrypt(this.id.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressWarnings("deprecation")
    public Long getUserId() {
        if (StringUtils.isEmpty(this.id)) {
            return 0L;
        }
        try {
            return Long.parseLong(AESUtil.decrypt(this.id.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @SuppressWarnings("deprecation")
    public JwtTokenUser encodeID(String id) {
        if (StringUtils.isEmpty(id)) {
            return this;
        }
        try {
            this.id = Long.parseLong(AESUtil.encrypt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

}
