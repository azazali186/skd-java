package com.street.core.auth_service.response;

import java.util.List;

import com.street.core.auth_service.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    UserEntity user;
    List<String> permissions;
}
