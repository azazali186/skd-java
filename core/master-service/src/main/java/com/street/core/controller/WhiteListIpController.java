package com.street.core.controller;

import com.street.core.request.WhiteListIpRequest;
import com.street.core.response.ApiResponse;
import com.street.core.service.WhiteListIpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "White List IP Management", description = "White List IP Management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/white-list-ip")
public class WhiteListIpController {
    @Autowired
    private WhiteListIpService whiteListService;

    @Operation(summary = "Admin Get All ip White List Configuration Data", description = "Admin Get All ip White List Configuration Data")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "", name = "ADMIN GET ALL IP WHITE LIST CONFIGURATION DATA", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAll(@RequestParam(required = false) String ipAddress,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        ApiResponse response = whiteListService.getAll(ipAddress, pageSize, pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Get Ip White List Configuration Data By Id", description = "Admin Get Ip White List Configuration Data By Id")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "/{id}", name = "ADMIN GET IP WHITE LIST CONFIGURATION DATA BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        ApiResponse response = whiteListService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Create Ip White List Data Configuration", description = "Admin Create Ip White List Data Configuration")
    @SuppressWarnings({ "rawtypes" })
    @PostMapping(value = "", name = "ADMIN CREATE IP WHITE LIST DATA CONFIGURATION", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody WhiteListIpRequest req) {
        ApiResponse response = whiteListService.create(req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Ip White List Configuration Data By Id", description = "Admin Ip White List Configuration Data By Id")
    @SuppressWarnings({ "rawtypes" })
    @PatchMapping(value = "/{id}", name = "ADMIN IP WHITE LIST CONFIGURATION DATA BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @RequestBody WhiteListIpRequest req) {
        ApiResponse response = whiteListService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
