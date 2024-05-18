package com.street.core.master_service.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.street.core.master_service.exception.NotFoundException;
import com.street.core.master_service.entity.AdminPageEntity;
import com.street.core.master_service.entity.PermissionEntity;
import com.street.core.master_service.entity.RoleEntity;
import com.street.core.master_service.entity.response.AdminPageResponse;
import com.street.core.master_service.entity.response.ChildrenPageResponse;
import com.street.core.master_service.entity.response.PermissionResponse;
import com.street.core.master_service.entity.response.RoleResponseEntity;
import com.street.core.master_service.entity.response.UserEntityResponse;
import com.street.core.master_service.enums.JwtTokenUser;
import com.street.core.master_service.enums.LoginUserUtils;
import com.street.core.master_service.repository.AdminPageRepo;
import com.street.core.master_service.repository.PermissionRepo;
import com.street.core.master_service.repository.RoleRepo;
import com.street.core.master_service.repository.UserRepo;
import com.street.core.master_service.request.ChildrenPermissionRequest;
import com.street.core.master_service.request.PermissionRequest;
import com.street.core.master_service.request.RoleRequest;
import com.street.core.auth_service.response.ApiResponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private LoginUserUtils loginUserUtils;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PermissionRepo permRepo;

    @Autowired
    private AdminPageRepo adminPageRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public ApiResponse getAllRoles() {
        List<RoleEntity> roles = StreamSupport.stream(roleRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
        ApiResponse<RoleEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Successfully retrieved all",
                roles);
        return response;
    }

    public ApiResponse getRole(Long id) {
        ApiResponse<RoleResponseEntity> response = new ApiResponse<>();
        Optional<RoleEntity> optionalRole = roleRepo.findById(id);
        if (!optionalRole.isPresent()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("INVALID_ROLE_ID");
        }

        RoleEntity role = optionalRole.get();

        RoleResponseEntity data = RoleResponseEntity.builder().id(role.getId()).build();
        List<AdminPageResponse> adminPage = getAdminPageResponse(role.getPermissions());
        data.setAdminPage(adminPage);

        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Successfully");
        response.setData(data);

        return response;
    }

    public List<AdminPageResponse> getAdminPageResponse(List<PermissionEntity> permissions) {
        List<AdminPageResponse> adminPages = new ArrayList<>();

        List<AdminPageEntity> adminPageEntities = getAdminPagesWithChildrenAndNullParents();

        adminPageEntities.forEach(adminPageEntity -> {
            boolean adminPageStatus = false;

            if (permissions != null) {
                adminPageStatus = adminPageEntity.getChildren().stream()
                        .flatMap(childPage -> childPage.getPermissions().stream())
                        .anyMatch(permissionEntity -> permissions.stream()
                                .anyMatch(pm -> pm.getId().equals(permissionEntity.getId())));
            }

            List<ChildrenPageResponse> childrenPageResponses = new ArrayList<>();

            adminPageEntity.getChildren().forEach(childPage -> {
                boolean childPageStatus = false;

                if (permissions != null) {
                    childPageStatus = childPage.getPermissions().stream()
                            .anyMatch(permissionEntity -> permissions.stream()
                                    .anyMatch(pm -> pm.getId().equals(permissionEntity.getId())));
                }

                ChildrenPageResponse childrenPageResponse = ChildrenPageResponse.builder()
                        .id(childPage.getId())
                        .name(childPage.getName())
                        .icon(childPage.getIcon())
                        .route(childPage.getRouteName())
                        .status(childPageStatus)
                        .build();

                List<PermissionResponse> permissionResponses = childPage.getPermissions().stream()
                        .map(permissionEntity -> {
                            boolean hasPermission = false;
                            if (permissions != null) {
                                hasPermission = permissions.stream()
                                        .anyMatch(pm -> pm.getId().equals(permissionEntity.getId()));
                            }
                            return new PermissionResponse(permissionEntity.getId(), permissionEntity.getName(),
                                    permissionEntity.getRoute(), hasPermission);
                        })
                        .collect(Collectors.toList());

                childrenPageResponse.setPermissions(permissionResponses);
                childrenPageResponses.add(childrenPageResponse);
            });

            AdminPageResponse adminPageResponse = AdminPageResponse.builder()
                    .id(adminPageEntity.getId())
                    .name(adminPageEntity.getName())
                    .icon(adminPageEntity.getIcon())
                    .route(adminPageEntity.getRouteName())
                    .status(adminPageStatus) // Set admin page status based on child statuses
                    .childrens(childrenPageResponses)
                    .build();

            adminPages.add(adminPageResponse);
        });

        return adminPages;
    }

    @Transactional
    public ApiResponse createRole(RoleRequest req) {

        try {
            Optional<RoleEntity> optionalRole = roleRepo.findById(req.getId());
            if (optionalRole.isPresent()) {
                return ApiResponse.error(409, "ROLE_NAME_EXIST");
            }

            JwtTokenUser tokenUser = loginUserUtils.getTokenUser();
            Optional<UserEntityResponse> user = userRepo.findById(tokenUser.getId());
            UserEntityResponse createdBy = user.orElse(null);

            RoleEntity role = new RoleEntity();

            role.setId(req.getId());

            role.setPermissions(getPermissions(req.getPermissions(), false, createdBy));

            roleRepo.save(role);

            return ApiResponse.success();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback the transaction
            return ApiResponse.error(409, e.getMessage());
        }
    }

    @SuppressWarnings("null")
    private List<PermissionEntity> getPermissions(List<PermissionRequest> permissions, boolean isUpdate,
            UserEntityResponse createdBy) throws NotFoundException {
        Iterable<PermissionEntity> permissionsEntity = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        for (PermissionRequest permReq : permissions) {
            Optional<AdminPageEntity> adminPageOptional = adminPageRepo.findById(permReq.getAdminPageId());
            if (!adminPageOptional.isPresent()) {
                throw new NotFoundException("ADMIN_PAGE_NOT_EXIST_WITH_ID_" + permReq.getAdminPageId());
            }
            AdminPageEntity adminPage = adminPageOptional.get();
            List<ChildrenPermissionRequest> childrens = permReq.getChildrens();
            for (ChildrenPermissionRequest child : childrens) {
                Optional<AdminPageEntity> childPageOptional = adminPageRepo.findById(child.getChildrenId());
                if (!childPageOptional.isPresent()) {
                    throw new NotFoundException("CHILDREN_PAGE_NOT_EXIST_WITH_ID_" + child.getChildrenId());
                }
                AdminPageEntity childPage = childPageOptional.get();
                permissionsEntity = null;
                permissionsEntity = permRepo.findAllById(child.getPermissionsIds());
                childPage.setPermissions(StreamSupport.stream(permissionsEntity.spliterator(), false)
                        .collect(Collectors.toList()));
                if (!isUpdate) {
                    childPage.setCreatedAt(new Date());
                    childPage.setCreatedBy(createdBy);
                }
                if (isUpdate) {
                    childPage.setUpdatedAt(new Date());
                    childPage.setUpdatedBy(createdBy);
                }
                adminPageRepo.save(childPage);
                ids.addAll(child.getPermissionsIds());
            }
            if (isUpdate) {
                adminPage.setUpdatedAt(new Date());
                adminPage.setUpdatedBy(createdBy);
            }
            if (!isUpdate) {
                adminPage.setCreatedAt(new Date());
                adminPage.setCreatedBy(createdBy);
            }
            adminPageRepo.save(adminPage);
        }
        permissionsEntity = null;
        permissionsEntity = permRepo.findAllById(ids);

        return StreamSupport.stream(permissionsEntity.spliterator(), false)
                .collect(Collectors.toList());
    }

    @SuppressWarnings({ "null", "rawtypes" })
    @Transactional
    public ApiResponse updatedRole(Long id, RoleRequest req) {

        if (id == 1) {
            return ApiResponse.error(400, "INVALID_ROLE_ID");
        }

        Optional<RoleEntity> optionalRole = roleRepo.findById(id);
        if (!optionalRole.isPresent()) {
            return ApiResponse.error(409, "ROLE_NAME_EXIST");
        }

        JwtTokenUser tokenUser = loginUserUtils.getTokenUser();
        Optional<UserEntityResponse> user = userRepo.findById(tokenUser.getId());
        UserEntityResponse updatedBy = user.orElse(null);

        RoleEntity role = optionalRole.get();

        if (req.getPermissions() != null) {
            role.getPermissions().clear();
        }

        roleRepo.save(role);

        if (req.getPermissions() != null) {
            List<PermissionEntity> permissions = getCPermissions(req, updatedBy, role);

            role.setPermissions(permissions);
        }

        roleRepo.save(role);

        return ApiResponse.success();
    }

    private List<PermissionEntity> getCPermissions(RoleRequest req, UserEntityResponse createdBy, RoleEntity role) {
        List<PermissionEntity> permissions = getPermissions(req.getPermissions(), false, createdBy);
        List<PermissionEntity> cPermissions = new ArrayList<>(role.getPermissions());

        Set<PermissionEntity> uniquePermissions = new HashSet<>(cPermissions);
        uniquePermissions.addAll(permissions);
        List<PermissionEntity> resultPermissions = new ArrayList<>(uniquePermissions);
        role.setPermissions(resultPermissions);

        return resultPermissions;
    }

    public List<AdminPageEntity> getAdminPagesWithChildrenAndNullParents() {
        String jpql = "SELECT ap FROM AdminPageEntity ap LEFT JOIN FETCH ap.children WHERE ap.parent IS NULL";
        TypedQuery<AdminPageEntity> query = entityManager.createQuery(jpql, AdminPageEntity.class);
        return query.getResultList();
    }

    public ApiResponse getRolePermissions() {
        List<AdminPageResponse> adminPage = getAdminPageResponse(null);
        ApiResponse<List<AdminPageResponse>> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Successfully");
        response.setData(adminPage);
        return response;
    }

}
