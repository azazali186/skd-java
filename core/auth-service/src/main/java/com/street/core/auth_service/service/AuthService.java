package com.street.core.auth_service.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.street.core.auth_service.entity.PermissionEntity;
import com.street.core.auth_service.entity.RoleEntity;
import com.street.core.auth_service.entity.SessionEntity;
import com.street.core.auth_service.entity.UserEntity;
import com.street.core.auth_service.entity.response.AdminPageResponse;
import com.street.core.auth_service.entity.response.UserEntityResponse;
import com.street.core.auth_service.exception.InvalidCredentialsException;
import com.street.core.auth_service.exception.MissingFieldException;
import com.street.core.auth_service.exception.UserAlreadyExistsException;
import com.street.core.auth_service.exception.UserNotFoundException;
import com.street.core.auth_service.repository.AuthRepo;
import com.street.core.auth_service.repository.PermissionRepo;
import com.street.core.auth_service.repository.RoleRepo;
import com.street.core.auth_service.repository.SessionRepo;
import com.street.core.auth_service.repository.UserRepo;
import com.street.core.auth_service.request.LoginRequest;
import com.street.core.auth_service.request.RegisterRequest;
import com.street.common.utils.ApiResponse;
import com.street.core.auth_service.response.UserWithToken;
import com.street.core.auth_service.utils.JwtUtil;

@Service
public class AuthService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity appUser = authRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        appUser.getRole().getPermissions().size(); // Ensure permissions are loaded
        List<PermissionEntity> permissions = appUser.getRole().getPermissions();

        List<GrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(),
                authorities);
    }

    public List<GrantedAuthority> getAuthorities() {

        List<PermissionEntity> permissions = StreamSupport.stream(permRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
        List<GrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

        return authorities;
    }

    @SuppressWarnings("rawtypes")
    public ApiResponse registerHandler(RegisterRequest req, String roleName) throws UserAlreadyExistsException {
        if (req.getUsername() == null || req.getPassword() == null) {
            throw new MissingFieldException("Email, Username, and Password are required fields.");
        }

        Optional<UserEntity> existingUser = authRepo.findByUsernameAndRoleName(req.getUsername(), roleName);

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with the same username already exists.");
        }

        Optional<RoleEntity> existRole = roleRepo.findByName(roleName);

        RoleEntity role = new RoleEntity();

        if (existRole.isPresent()) {
            role = existRole.get();
        } else {
            role.setName(roleName);
            role.setDesc(roleName + " ROLE");
            roleRepo.save(role);
            role = roleRepo.findByName(roleName).get();

        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(req.getUsername());
        String hashedPassword = hashPassword(req.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setCreatedAt(new Date());
        newUser.setRole(role);
        newUser.setCreatedAt(new Date());
        newUser.setStatus(true);
        authRepo.save(newUser);

        LOGGER.info("User created: {}", newUser);

        newUser.setPassword(null);

        UserEntityResponse user = userRepo.findById(newUser.getId()).get();

        ApiResponse<UserEntityResponse> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User registered successfully",
                user);

        return response;
    }

    public String hashPassword(String plainTextPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(plainTextPassword);
    }

    @SuppressWarnings({ "rawtypes", "null" })
    public ApiResponse loginHandler(LoginRequest req, String roleName)
            throws UserNotFoundException, InvalidCredentialsException {
        Optional<UserEntity> optionalUserEntity = authRepo.findByUsernameAndRoleName(req.getUsername(), roleName);

        // If user not found, throw exception
        if (!optionalUserEntity.isPresent()) {
            throw new UserNotFoundException("User not found with email: " + req.getUsername());
        }
        UserEntity userEntity = optionalUserEntity.get();

        if (!isPasswordValid(req.getPassword(), userEntity.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        // userEntity.setPassword(null);

        String jwtToken = JwtUtil.getInstance().generateToken(req.getUsername(), userEntity.getId());

        // Iterable<PermissionEntity> permissions = permRepo.findAll();

        List<AdminPageResponse> adminPage = roleService.getAdminPageResponse(userEntity.getRole().getPermissions());

        UserEntityResponse ur = userRepo.findById(userEntity.getId()).get();

        UserWithToken userWithToken = new UserWithToken(ur, jwtToken, adminPage);

        userEntity.setLastLogin(new Date());
        authRepo.save(userEntity);
        ApiResponse response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Login successful",
                userWithToken);

        return response;
    }

    private boolean isPasswordValid(String plainTextPassword, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(plainTextPassword, hashedPassword);
    }

    public UserEntity findById(Long id) throws UserNotFoundException {
        Optional<UserEntity> optionalUserEntity = authRepo.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        return optionalUserEntity.get();
    }

    @SuppressWarnings("rawtypes")
    public ApiResponse getUserById(Long id) {

        UserEntity user = findById(id);

        user.setPassword(null);

        ApiResponse<UserEntity> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Successfully",
                user);

        return response;
    }

    public SessionEntity getSessionInfo(String secretKey, String ipAddress) {
        Optional<SessionEntity> optionalSession = sessionRepo.findBySecretAndIpAddress(secretKey, ipAddress);
        SessionEntity session = null;

        if (optionalSession.isPresent()) {
            session = optionalSession.get();
        }
        return session;
    }

}
