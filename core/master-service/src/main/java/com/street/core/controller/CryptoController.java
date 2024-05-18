package com.street.core.controller;
import com.street.core.request.CryptoRequest;
import com.street.core.response.ApiResponse;
import com.street.core.service.CryptoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Crypto Management", description = "Crypto Management APIs")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("crypto-currencies")
public class CryptoController {
    @Autowired
    private CryptoService cryptoService;

    @Operation(summary = "Admin Get All Crypto Currencies", description = "Admin Get All Crypto Currencies")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "", name = "ADMIN GET ALL CRYPTO CURRENCIES", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getAll(@RequestParam(required = false) String name,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer pageNumber) {

        ApiResponse response = cryptoService.getAll(name, pageSize, pageNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Get By id Crypto Currency", description = "Admin Get By id Crypto Currency")
    @SuppressWarnings({ "rawtypes" })
    @GetMapping(value = "/{id}", name = "ADMIN GET BY ID CRYPTO CURRENCY", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        ApiResponse response = cryptoService.getById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Create New Crypto Currency", description = "Admin Create New Crypto Currency")
    @SuppressWarnings({ "rawtypes" })
    @PostMapping(value = "", name = "ADMIN CREATE NEW CRYPTO CURRENCY", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> create(@RequestBody CryptoRequest req) {
        ApiResponse response = cryptoService.create(req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Admin Update Crypto Currency By Id", description = "Admin Update Crypto Currency By Id")
    @SuppressWarnings({ "rawtypes" })
    @PatchMapping(value = "/{id}", name = "ADMIN UPDATE CRYPTO CURRENCY BY ID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @RequestBody CryptoRequest req) {
        ApiResponse response = cryptoService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
