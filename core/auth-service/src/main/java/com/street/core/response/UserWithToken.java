package com.street.core.response;

import java.util.List;

import com.street.core.entity.response.AdminPageResponse;
import com.street.core.entity.response.UserEntityResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithToken {
    private UserEntityResponse user;
    private String token;
    private List<AdminPageResponse> permissions;
}
