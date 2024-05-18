package com.street.core.auth_service.entity.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseEntity {
    Long id;
    String name;
    String desc;
    List<AdminPageResponse> adminPage;
}
