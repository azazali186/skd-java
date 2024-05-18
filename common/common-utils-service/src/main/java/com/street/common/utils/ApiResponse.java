package com.street.common.utils;

import java.util.HashMap;
import java.util.List;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, String message, List<T> data) {
        this.status = status;
        this.message = message;
        HashMap<String, List<T>> dataObj = new HashMap<>();
        dataObj.put("data", data);
        this.data = (T) dataObj;
    }

    public ApiResponse(int status) {
        this.status = status;
    }

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }


    public static ApiResponse<Object> successPage(List list, Long count) {
        return successPage(list, count, null);
    }

    public static ApiResponse<Object> successPage(List list, Long count, HashMap<String, Object> sumMap) {
        ApiResponse<Object> apiResult = new ApiResponse();
        PageData pageData = PageData.data(list, count);
        pageData.setSumMap(sumMap);
        apiResult.data = pageData;
        apiResult.status = HttpStatus.OK.value();
        apiResult.message = "Success";
        return apiResult;
    }

    public static ApiResponse<Object> success() {
        return new ApiResponse<>(HttpStatus.OK.value(), "Success");
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Success", data);
    }

    public static ApiResponse<Object> failNotFound(String message) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), message);
    }

    public static ApiResponse<Object> badRequest(String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static ApiResponse<Object> error(int i, String message) {
        return new ApiResponse<>(i, message);
    }
}

