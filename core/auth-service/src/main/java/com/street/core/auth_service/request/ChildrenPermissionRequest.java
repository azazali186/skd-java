package com.street.core.auth_service.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildrenPermissionRequest {
    Long childrenId;
    List<Long> permissionsIds;
}
