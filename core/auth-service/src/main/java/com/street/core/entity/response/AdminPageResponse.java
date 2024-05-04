package com.street.core.entity.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPageResponse {
    Long id;
    String name;
    String icon;
    String route;
    Boolean status = false;
    List<ChildrenPageResponse> childrens;
}
