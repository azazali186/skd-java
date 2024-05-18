package com.street.core.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.street.core.entity.PermissionEntity;
import com.street.core.entity.RoleEntity;
import com.street.core.entity.SessionEntity;
import com.street.core.entity.UserEntity;
import com.street.core.entity.response.AdminPageResponse;
import com.street.core.entity.response.UserEntityResponse;
import com.street.core.exception.InvalidCredentialsException;
import com.street.core.exception.MissingFieldException;
import com.street.core.exception.UserAlreadyExistsException;
import com.street.core.exception.UserNotFoundException;
import com.street.core.repository.AuthRepo;
import com.street.core.repository.PermissionRepo;
import com.street.core.repository.RoleRepo;
import com.street.core.repository.SessionRepo;
import com.street.core.repository.UserRepo;
import com.street.core.request.LoginRequest;
import com.street.core.request.RegisterRequest;
import com.street.core.response.ApiResponse;
import com.street.core.response.AuthResponse;
import com.street.core.response.UserWithToken;
import com.street.core.utils.JwtUtil;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private PermissionRepo permRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepo userRepo;

    // // @Override
    public AuthResponse loadUserByUserId(Long userId) throws UserNotFoundException {
        UserEntity appUser = authRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found with id: " + userId));
        appUser.getRole().getPermissions().size();
        List<PermissionEntity> permissions = appUser.getRole().getPermissions();

        List<String> authorities = permissions.stream()
                .map(permission -> (permission.getName()))
                .collect(Collectors.toList());

        return AuthResponse.builder().user(appUser).permissions(authorities).build();
    }

    // public List<GrantedAuthority> getAuthorities() {

    // List<PermissionEntity> permissions =
    // StreamSupport.stream(permRepo.findAll().spliterator(), false)
    // .collect(Collectors.toList());
    // List<GrantedAuthority> authorities = permissions.stream()
    // .map(permission -> new SimpleGrantedAuthority(permission.getName()))
    // .collect(Collectors.toList());

    // return authorities;
    // }
    public UserEntity findById(Long id) throws UserNotFoundException {
        Optional<UserEntity> optionalUserEntity = authRepo.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        return optionalUserEntity.get();
    }

}
