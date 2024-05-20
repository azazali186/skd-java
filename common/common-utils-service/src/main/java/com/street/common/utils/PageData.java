package com.street.common.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageData {

    List<Object> list;

    Object count;

    Object sumMap;

    public static PageData data(List<Object> list, int count) {
        return builder().list(list).count(count).build();
    }

    public static PageData data(List<Object> list, Long count) {
        return builder().list(list).count(count).build();
    }

}
