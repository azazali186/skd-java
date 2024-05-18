package com.street.core.controller;
import com.street.core.request.AdminPageRequest;
import com.street.core.response.ApiResponse;
import com.street.core.service.AdminPageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "AdminPage Management", description = "AdminPage Management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("admin-pages")
public class AdminPageController {
    @Autowired
    private AdminPageService adminPageService;

    @Operation(summary = "Admin Get All Admin Pages With Permissions", description = "Admin Get All Admin Pages With Permissions")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "", name = "ADMIN GET ALL ADMIN PAGES WITH PERMISSIONS", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAll(@RequestParam(required = false) String name,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        ApiResponse response = adminPageService.getAll(name, pageSize, pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Get By id Admin Pages With Permissions", description = "Admin Get By id Admin Pages With Permissions")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "/{id}", name = "ADMIN GET BY ID ADMIN PAGES WITH PERMISSIONS", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        ApiResponse response = adminPageService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Create New Admin Pages With Permissions", description = "Admin Create New Admin Pages With Permissions")
    @SuppressWarnings({ "rawtypes" })
    @PostMapping(value = "", name = "ADMIN CREATE NEW ADMIN PAGES WITH PERMISSIONS", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody AdminPageRequest req) {
        ApiResponse response = adminPageService.create(req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Update By Id Admin Pages With Permissions", description = "Admin Update By Id Admin Pages With Permissions")
    @SuppressWarnings({ "rawtypes" })
    @PatchMapping(value = "/{id}", name = "ADMIN UPDATE BY ID ADMIN PAGES WITH PERMISSIONS", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @RequestBody AdminPageRequest req) {
        ApiResponse response = adminPageService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
