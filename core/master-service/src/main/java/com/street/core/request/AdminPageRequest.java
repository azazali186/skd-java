package com.street.core.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminPageRequest {

    List<Long> permissionIds;

    String name;

    String description;

    String icon;

    String url;

    String routeName;

    List<Long> childrenIds;

    Long parentId;

    Boolean status;
}
