package com.street.core.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.street.core.entity.RoleEntity;
import com.street.core.entity.UserEntity;
import com.street.core.entity.response.UserEntityResponse;
import com.street.core.enums.JwtTokenUser;
import com.street.core.enums.LoginUserUtils;
import com.street.core.repository.AuthRepo;
import com.street.core.repository.RoleRepo;
import com.street.core.repository.UserRepo;
import com.street.core.request.UserRequest;
import com.street.core.response.ApiResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthService authService;

    @Autowired
    private LoginUserUtils loginUserUtils;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    public ApiResponse createUser(@Valid UserRequest req) {
        try {
            if (req.getUsername() == null) {
                return ApiResponse.error(400, "USERNAME_IS_REQUIRED");
            }

            if (req.getPassword() == null) {
                return ApiResponse.error(400, "PASSWORD_IS_REQUIRED");
            }

            if (req.getRoleId() == null) {
                return ApiResponse.error(400, "ROLE_ID_IS_REQUIRED");
            }

            Optional<UserEntity> existingUser = authRepo.findByUsername(req.getUsername());

            if (existingUser.isPresent()) {
                return ApiResponse.error(409, "User with the same username already exists.");
            }

            Optional<RoleEntity> existRole = roleRepo.findById(req.getRoleId());

            if (!existRole.isPresent()) {
                return ApiResponse.error(404, "Invalid Role ID");
            }

            JwtTokenUser tokenUser = loginUserUtils.getTokenUser();
            Optional<UserEntityResponse> user = userRepo.findById(tokenUser.getId());
            UserEntityResponse createdBy = user.orElse(null);

            RoleEntity role = existRole.get();

            UserEntity newUser = new UserEntity();
            newUser.setUsername(req.getUsername());
            String hashedPassword = authService.hashPassword(req.getPassword());
            newUser.setPassword(hashedPassword);
            newUser.setCreatedAt(new Date());
            newUser.setRole(role);
            newUser.setCreatedAt(new Date());
            newUser.setCreatedBy(createdBy);
            newUser.setStatus(true);
            authRepo.save(newUser);

            return ApiResponse.success();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback the transaction
            return ApiResponse.error(400, "ERROR_CREATE_USER");
        }
    }

    public ApiResponse updateUser(@Valid Long id, @Valid UserRequest req) {
        try {

            Optional<UserEntity> existingUser = authRepo.findById(id);

            if (!existingUser.isPresent()) {
                return ApiResponse.error(409, "Invalid user id.");
            }

            UserEntity user = existingUser.get();

            if (req.getPassword() != null) {
                user.setPassword(authService.hashPassword(req.getPassword()));
            }
            if (req.getRoleId() != null) {
                Optional<RoleEntity> existRole = roleRepo.findById(req.getRoleId());
                if (!existRole.isPresent()) {
                    return ApiResponse.error(404, "Invalid Role ID");
                }
                RoleEntity role = existRole.get();
                user.setRole(role);
            }

            JwtTokenUser tokenUser = loginUserUtils.getTokenUser();
            Optional<UserEntityResponse> userE = userRepo.findById(tokenUser.getId());
            UserEntityResponse updatedBy = userE.orElse(null);

            user.setUpdatedBy(updatedBy);
            user.setUpdatedAt(new Date());

            authRepo.save(user);

            return ApiResponse.success();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // Rollback the transaction
            return ApiResponse.error(400, "ERROR_UPDATE_USER");
        }
    }

    public ApiResponse getAll(String username, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page data = authRepo.findUsers(username, pageable);
        return ApiResponse.successPage(data.getContent(), data.getTotalElements());
    }

    public ApiResponse getUser(@Valid Long id) {
        Optional<UserEntity> existingUser = authRepo.findById(id);

        if (!existingUser.isPresent()) {
            return ApiResponse.error(409, "Invalid user id.");
        }

        return ApiResponse.success(existingUser.get());
    }

}
