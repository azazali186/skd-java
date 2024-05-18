package com.street.core.master_service.controller;

import com.street.core.master_service.request.AcceptedCurrenciesRequest;
import com.street.core.master_service.service.AcceptedCurrenciesService;
import com.street.core.auth_service.response.ApiResponse;.ApiResponse;

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

@Tag(name = "AcceptedCurrencies Management", description = "AcceptedCurrencies Management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/accepted-currencies")
public class AcceptedCurrenciesController {
    @Autowired
    private AcceptedCurrenciesService currencyService;

    @Operation(summary = "Admin Get All Accepted Currencies", description = "Admin Get All Accepted Currencies")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "", name = "ADMIN GET ALL ACCEPTED CURRENCIES", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAll(@RequestParam(required = false, name = "name") String name,
            @RequestParam(defaultValue = "10", required = false, name = "pageSize") Integer pageSize,
            @RequestParam(defaultValue = "0", required = false, name = "pageNumber") Integer pageNumber) {

        ApiResponse response = currencyService.getAll(name, pageSize, pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Get By Id Accepted Currencies", description = "Admin Get By Id Accepted Currencies")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "/{id}", name = "ADMIN GET BY ID ACCEPTED CURRENCIES", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        ApiResponse response = currencyService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Create Accepted Currencies", description = "Admin Create Accepted Currencies")
    @SuppressWarnings({ "rawtypes" })
    @PostMapping(value = "", name = "ADMIN CREATE ACCEPTED CURRENCIES", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody AcceptedCurrenciesRequest req) {
        ApiResponse response = currencyService.create(req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Update By Id Accepted Currencies", description = "Admin Update By Id Accepted Currencies")
    @SuppressWarnings({ "rawtypes" })
    @PatchMapping(value = "/{id}", name = "ADMIN UPDATE BY ID ACCEPTED CURRENCIES", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @RequestBody AcceptedCurrenciesRequest req) {
        ApiResponse response = currencyService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
