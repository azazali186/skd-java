package com.street.core.response;

import java.util.List;

import com.street.core.entity.UserEntity;

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
