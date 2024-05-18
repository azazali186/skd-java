package com.street.core.master_service.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "USERNAME_IS_REQUIRED")
    @NotNull(message = "USERNAME_CAN_NOT_NULL")
    String username;

    @NotEmpty(message = "PASSWORD_IS_REQUIRED")
    @NotNull(message = "PASSWORD_CAN_NOT_NULL")
    String password;
}
