package com.street.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.street.core.entity.PermissionEntity;
import com.street.core.entity.RoleEntity;
import com.street.core.entity.UserEntity;
import com.street.core.repository.AuthRepo;
import com.street.core.repository.PermissionRepo;
import com.street.core.repository.RoleRepo;
import com.street.core.service.AuthService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import org.springframework.web.method.HandlerMethod;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RouteNameFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteNameFetcher.class);

    @Autowired
    PermissionRepo permRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    AuthRepo authRepo;

    @Autowired
    AuthService authService;

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    @PostConstruct
    public void fetchRouteNames() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        List<PermissionEntity> allPermissions = new ArrayList<>();

        for (RequestMappingInfo requestMappingInfo : handlerMethods.keySet()) {
            String name = requestMappingInfo.getName();
            String route = "";
            if (!requestMappingInfo.getPatternValues().isEmpty()) {
                route = requestMappingInfo.getPatternValues().toString().replace("[", "").replace("]", "");
            }
            if (name != null) {
                PermissionEntity permissions = permRepo.findByName(name);

                if (permissions == null) {
                    permissions = new PermissionEntity();
                    permissions.setName(name);
                    permissions.setRoute(route);
                    permissions.setMethod(requestMappingInfo.getMethodsCondition().getMethods().toString()
                            .replace("[", "").replace("]", ""));
                    permissions.setGuard("API");
                } else {
                    permissions.setRoute(route);
                    permissions.setMethod(requestMappingInfo.getMethodsCondition().getMethods().toString()
                            .replace("[", "").replace("]", ""));
                }
                permRepo.save(permissions);
                allPermissions.add(permissions);
            }

        }

        Optional<RoleEntity> optionalRole = roleRepo.findByName("ADMIN");
        RoleEntity adminRole;
        if (optionalRole.isPresent()) {
            adminRole = optionalRole.get();
        } else {
            adminRole = new RoleEntity();
            adminRole.setCreatedAt(new Date());
            adminRole.setName("ADMIN");
            adminRole.setIsPublic(false);
            adminRole.setDesc("Admin role"); // Set a description if needed
        }
        roleRepo.save(adminRole);
        if (!allPermissions.isEmpty()) {
            adminRole.setPermissions(allPermissions);
        }
        Optional<RoleEntity> optionalMemberRole = roleRepo.findByName("MEMBER");
        RoleEntity memberRole;
        if (optionalMemberRole.isPresent()) {
            memberRole = optionalMemberRole.get();
        } else {
            memberRole = new RoleEntity();
            memberRole.setCreatedAt(new Date());
            memberRole.setName("MEMBER");
            memberRole.setIsPublic(false);
            memberRole.setDesc("Member role"); // Set a description if needed
        }
        roleRepo.save(memberRole);

        Optional<RoleEntity> optionalSupplierRole = roleRepo.findByName("VENDOR");
        RoleEntity vendor;
        if (optionalSupplierRole.isPresent()) {
            vendor = optionalSupplierRole.get();
        } else {
            vendor = new RoleEntity();
            vendor.setCreatedAt(new Date());
            vendor.setName("VENDOR");
            vendor.setIsPublic(false);
            vendor.setDesc("Vendor role"); // Set a description if needed
        }
        roleRepo.save(vendor);

        Optional<UserEntity> existingUser = authRepo.findByUsername("admin");

        if (!existingUser.isPresent()) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername("admin");
            String hashedPassword = authService.hashPassword("123456");
            newUser.setPassword(hashedPassword);
            newUser.setCreatedAt(new Date());
            newUser.setRole(adminRole);
            newUser.setCreatedAt(new Date());
            newUser.setCreatedBy(null);
            newUser.setStatus(true);
            authRepo.save(newUser);
        }

        roleRepo.save(adminRole);
    }
}
