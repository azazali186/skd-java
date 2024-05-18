package com.street.core.auth_service.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.street.core.auth_service.request.RoleRequest;
import com.street.core.auth_service.response.ApiResponse;
import com.street.core.auth_service.service.RoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Roles Management", description = "Roles management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @SuppressWarnings({ "rawtypes", "null" })
    @GetMapping(value = "", name = "GET ALL ROLES", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAllRoles() {
        ApiResponse response = roleService.getAllRoles();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @GetMapping(value = "/permissions", name = "GET PERMISSIONS FOR ROLE", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getPermissions() {
        ApiResponse response = roleService.getRolePermissions();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @SuppressWarnings({ "rawtypes", "null" })
    @GetMapping(value = "/{id}", name = "GET ROLE BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getRole(@PathVariable("id") Long id) {
        ApiResponse response = roleService.getRole(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @SuppressWarnings({ "null", "rawtypes" })
    @PostMapping(value = "", name = "ADMIN CREATE NEW ROLE", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> createRole(@RequestBody RoleRequest req) {
        ApiResponse response = roleService.createRole(req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @SuppressWarnings({ "null", "rawtypes" })
    @PatchMapping(value = "/{id}", name = "ADMIN UPDATE ROLE BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updatedRole(@PathVariable("id") Long id, @RequestBody RoleRequest req) {
        ApiResponse response = roleService.updatedRole(id,req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
