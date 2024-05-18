package com.street.core.auth_service.controller;

import com.alibaba.fastjson.JSON;
import com.street.common.utils.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.street.core.auth_service.request.LoginRequest;
import com.street.core.auth_service.request.RegisterRequest;
import com.street.core.auth_service.response.ApiResponse;
import com.street.core.auth_service.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Tag(name = "Auth Management", description = "Authentication management APIs")
@RestController
@RequestMapping("auth")
@Validated
public class AuthController {

    @Autowired
    AuthService authService;

//    @Autowired
    MessagePublisher messagePublisher = new MessagePublisher();

    @Operation(summary = "Admin Registration", description = "Register user as role admin.")
    @SuppressWarnings({ "rawtypes" })
    @PostMapping("/admin/register")
    public ResponseEntity<ApiResponse> adminRegisterHandler(@RequestBody RegisterRequest req) {
        ApiResponse response = authService.registerHandler(req, "ADMIN");
        messagePublisher.publishMessage(null,"skd-register", JSON.toJSONString(response));
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Login", description = "Login user as role admin.")
    @SuppressWarnings({ "rawtypes" })
    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse> adminLoginHandler(@Valid @RequestBody LoginRequest req) {
        ApiResponse response = authService.loginHandler(req, "ADMIN");
        messagePublisher.publishMessage(null,"skd-login", JSON.toJSONString(response));
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @SuppressWarnings({ "rawtypes", "null" })
    @Operation(summary = "Member Registration", description = "Register user as role member.")
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> registerHandler(@RequestBody RegisterRequest req) {
        ApiResponse response = authService.registerHandler(req, "MEMBER");
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Member Login", description = "Login user as role member.")
    @SuppressWarnings({ "rawtypes", "null" })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginHandler(@Valid @RequestBody(required = true) LoginRequest req) {
        ApiResponse response = authService.loginHandler(req, "MEMBER");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    

    /* @SuppressWarnings({ "rawtypes", "null" })
    @GetMapping(value = "/user/{id}", name = "GET USER BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") Long id) {
        ApiResponse response = authService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    } */

    /* @Operation(summary = "Get Secret Key for Login", description = "Get Secret Key for Login.")
    @SuppressWarnings({ "rawtypes", "null" })
    @PostMapping("/get-secret-key")
    public ResponseEntity<ApiResponse> getSecretKey(@RequestBody SecretKeyRequest req, HttpServletRequest request) {
        ApiResponse response = authService.getSecretKey(req, request);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    } */

}
