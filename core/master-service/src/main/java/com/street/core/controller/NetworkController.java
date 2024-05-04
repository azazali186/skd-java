package com.street.core.controller;
import com.street.core.request.NetworkRequest;
import com.street.core.response.ApiResponse;
import com.street.core.service.NetworkService;

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


@Tag(name = "Network Management", description = "Network Management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("networks")
public class NetworkController {
    @Autowired
    private NetworkService networkService;

    @Operation(summary = "Admin Get All Network Configuration", description = "Admin Get All Network Configuration")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "", name = "ADMIN GET ALL NETWORK CONFIGURATIONS", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAll(@RequestParam(required = false) String name,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        ApiResponse response = networkService.getAll(name, pageSize, pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Get By id Network Configuration", description = "Admin Get By id Network Configuration")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "/{id}", name = "ADMIN GET BY ID NETWORK CONFIGURATION", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse response = networkService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Create New Network Configuration", description = "Admin Create New Network Configuration")
    @SuppressWarnings({ "rawtypes" })
    @PostMapping(value = "", name = "ADMIN CREATE NEW NETWORK CONFIGURATION", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody NetworkRequest req) {
        ApiResponse response = networkService.create(req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Update Network Configuration by Id", description = "Admin Update Network Configuration by Id")
    @SuppressWarnings({ "rawtypes" })
    @PatchMapping(value = "/{id}", name = "ADMIN UPDATE NETWORK CONFIGURATION BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody NetworkRequest req) {
        ApiResponse response = networkService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
