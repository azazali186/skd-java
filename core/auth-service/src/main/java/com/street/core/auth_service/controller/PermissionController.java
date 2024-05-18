package com.street.core.auth_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Permissions Management", description = "Permissions management APIs")
@RestController
@RequestMapping("/permissions")
@SecurityRequirement(name = "Authorization")
public class PermissionController {
    
}
