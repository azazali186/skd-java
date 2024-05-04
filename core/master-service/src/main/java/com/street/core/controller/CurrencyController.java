package com.street.core.controller;
import com.street.core.request.CurrencyRequest;
import com.street.core.response.ApiResponse;
import com.street.core.service.CurrencyService;

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


@Tag(name = "Currency Management", description = "Currency Management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/currencies")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @Operation(summary = "Admin Get All Currency", description = "Admin Get All Currency")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "", name = "ADMIN GET ALL CURRENCY", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAll(@RequestParam(required = false) String name,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        ApiResponse response = currencyService.getAll(name, pageSize, pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Get By id Currency", description = "Admin Get By id Currency")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "/{id}", name = "ADMIN GET BY ID CURRENCY", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse response = currencyService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Create Currency", description = "Admin Create Currency")
    @SuppressWarnings({ "rawtypes" })
    @PostMapping(value = "", name = "ADMIN CREATE CURRENCY", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody CurrencyRequest req) {
        ApiResponse response = currencyService.create(req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Update By id Currency", description = "Admin Update By id Currency")
    @SuppressWarnings({ "rawtypes" })
    @PatchMapping(value = "/{id}", name = "ADMIN UPDATE BY ID CURRENCY", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody CurrencyRequest req) {
        ApiResponse response = currencyService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
