package com.street.core.service;

import com.street.core.entity.AdminPageEntity;
import com.street.core.entity.PermissionEntity;
import com.street.core.entity.response.UserEntityResponse;
import com.street.core.enums.JwtTokenUser;
import com.street.core.enums.LoginUserUtils;
import com.street.core.repository.AdminPageRepo;
import com.street.core.repository.PermissionRepo;
import com.street.core.repository.UserRepo;
import com.street.core.request.AdminPageRequest;
import com.street.core.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminPageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPageService.class);

    @Autowired
    private AdminPageRepo adminPageRepo;

    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LoginUserUtils loginUserUtils;

    public ApiResponse getAll(String name, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page data = adminPageRepo.findAllAdminPage(name, pageable);
        return ApiResponse.successPage(data.getContent(), data.getTotalElements());
    }

    public ApiResponse getById(Long id) {
        Optional<AdminPageEntity> adminPage = adminPageRepo.findById(id);
        if (adminPage.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CURRENCY_ID");
        }
        return ApiResponse.success(adminPage.get());
    }

    public ApiResponse create(AdminPageRequest req) {

        JwtTokenUser tokenUser = loginUserUtils.getTokenUser();
        Optional<UserEntityResponse> user = userRepo.findById(tokenUser.getId());
        UserEntityResponse createdBy = user.orElse(null);

        AdminPageEntity adminPage = new AdminPageEntity();
        adminPage.setName(req.getName());
        adminPage.setIcon(req.getIcon());
        adminPage.setRouteName(req.getRouteName());
        adminPage.setUrl(req.getUrl());
        adminPage.setDesc(req.getDescription());

        if (req.getParentId() != null) {
            Optional<AdminPageEntity> opParent = adminPageRepo.findById(req.getParentId());
            if (opParent.isEmpty()) {
                return ApiResponse.error(404, "INVALID_PARENT_ID");
            }
            adminPage.setParent(opParent.get());
        }

        if (req.getChildrenIds() != null && req.getChildrenIds().size() > 0) {
            List<AdminPageEntity> childrens = adminPageRepo.findAllById(req.getChildrenIds());
            adminPage.setChildren(childrens);
        }

        if (req.getPermissionIds() != null && req.getPermissionIds().size() > 0) {
            List<PermissionEntity> permissions = permissionRepo.findAllById(req.getPermissionIds());
            adminPage.setPermissions(permissions);
        }

        adminPage.setCreatedBy(createdBy);
        adminPage.setCreatedAt(new Date());
        adminPage.setStatus(true);
        adminPageRepo.save(adminPage);
        return ApiResponse.success();
    }

    public ApiResponse update(Long id, AdminPageRequest req) {
        Optional<AdminPageEntity> opadminPage = adminPageRepo.findById(id);
        if (opadminPage.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CURRENCY_ID");
        }

        JwtTokenUser tokenUser = loginUserUtils.getTokenUser();
        Optional<UserEntityResponse> user = userRepo.findById(tokenUser.getId());
        UserEntityResponse createdBy = user.orElse(null);

        AdminPageEntity adminPage = opadminPage.get();

        if (req.getName() != null)
            adminPage.setName(req.getName());
        if (req.getIcon() != null)
            adminPage.setIcon(req.getIcon());
        if (req.getRouteName() != null)
            adminPage.setRouteName(req.getRouteName());
        if (req.getUrl() != null)
            adminPage.setUrl(req.getUrl());
        if (req.getDescription() != null)
            adminPage.setDesc(req.getDescription());

        if (req.getParentId() != null) {
            Optional<AdminPageEntity> opParent = adminPageRepo.findById(req.getParentId());
            if (opParent.isEmpty()) {
                return ApiResponse.error(404, "INVALID_PARENT_ID");
            }
            adminPage.setParent(opParent.get());
        }

        if (req.getChildrenIds() != null && req.getChildrenIds().size() > 0) {
            List<AdminPageEntity> childrens = adminPageRepo.findAllById(req.getChildrenIds());
            adminPage.setChildren(childrens);
        }

        if (req.getPermissionIds() != null && req.getPermissionIds().size() > 0) {
            List<PermissionEntity> permissions = permissionRepo.findAllById(req.getPermissionIds());
            adminPage.setPermissions(permissions);
        }
        if (req.getStatus() != null)
            adminPage.setStatus(req.getStatus());

        adminPage.setUpdatedBy(createdBy);
        adminPage.setUpdatedAt(new Date());
        adminPageRepo.save(adminPage);

        return ApiResponse.success();
    }

}
