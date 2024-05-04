package com.street.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.street.core.request.UserRequest;
import com.street.core.response.ApiResponse;
import com.street.core.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "User Management", description = "Users management APIs")
@RestController
@RequestMapping("users")
@Validated
@SecurityRequirement(name = "Authorization")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Operation(summary = "Admin Create user", description = "Admin Create users with role.")
    @SuppressWarnings({ "rawtypes", "null" })
    @PostMapping(value = "", name = "ADMIN CREATE NEW USER", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserRequest req) {
        ApiResponse response = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Update user", description = "Admin Update users with role.")
    @SuppressWarnings({ "rawtypes", "null" })
    @PatchMapping(value = "/{id}", name = "ADMIN UPDATE USER BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateUser(@Valid @PathVariable("id") Long id,@Valid @RequestBody UserRequest req) {
        ApiResponse response = userService.updateUser(id,req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Get All users", description = "Admin Get All users with role.")
    @SuppressWarnings({ "rawtypes", "null" })
    @GetMapping(value = "", name = "ADMIN GET ALL USER", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAll(@RequestParam(required = false) String username,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "0") Integer pageNumber) {
        ApiResponse response = userService.getAll(username, pageNumber, pageSize);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Get user by id", description = "Admin Get user by id with role.")
    @SuppressWarnings({ "rawtypes", "null" })
    @GetMapping(value = "/{id}", name = "ADMIN GET USER BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getUser(@Valid @PathVariable("id") Long id) {
        ApiResponse response = userService.getUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


}
