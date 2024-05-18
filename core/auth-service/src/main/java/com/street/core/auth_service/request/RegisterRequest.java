package com.street.core.auth_service.request;

import lombok.Data;

@Data
public class RegisterRequest {
    String username;
    String password;
}
