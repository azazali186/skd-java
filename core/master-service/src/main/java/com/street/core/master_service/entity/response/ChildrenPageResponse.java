package com.street.core.master_service.entity.response;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildrenPageResponse {
    Long id;
    String name;
    String icon;
    String route;
    Boolean status = false;
    List<PermissionResponse> permissions;
}
