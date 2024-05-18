package com.street.core.master_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.street.core.master_service.entity.PermissionEntity;
import com.street.core.master_service.entity.RoleEntity;
import com.street.core.master_service.entity.UserEntity;
import com.street.core.master_service.repository.AuthRepo;
import com.street.core.master_service.repository.PermissionRepo;
import com.street.core.master_service.repository.RoleRepo;
import com.street.core.master_service.service.AuthService;

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

        Optional<RoleEntity> optionalRole = roleRepo.findById(1);
        RoleEntity adminRole;
        if (optionalRole.isPresent()) {
            adminRole = optionalRole.get();
        } else {
            adminRole = new RoleEntity();
        }
        roleRepo.save(adminRole);
        if (!allPermissions.isEmpty()) {
            adminRole.setPermissions(allPermissions);
        }
        Optional<RoleEntity> optionalMemberRole = roleRepo.findById(2);
        RoleEntity memberRole;
        if (optionalMemberRole.isPresent()) {
            memberRole = optionalMemberRole.get();
        } else {
            memberRole = new RoleEntity();
        }
        roleRepo.save(memberRole);

        Optional<RoleEntity> optionalSupplierRole = roleRepo.findById(3);
        RoleEntity vendor;
        if (optionalSupplierRole.isPresent()) {
            vendor = optionalSupplierRole.get();
        } else {
            vendor = new RoleEntity();
        }
        roleRepo.save(vendor);

        Optional<UserEntity> existingUser = authRepo.findById(Long.valueOf(1));

        if (existingUser.isEmpty()) {
            UserEntity newUser = new UserEntity();
            newUser.setRole(adminRole);
            authRepo.save(newUser);
        }
        roleRepo.save(adminRole);
    }
}
