// package com.street.core.config;

// import java.util.List;
// import java.util.stream.Collectors;
// import java.util.stream.StreamSupport;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.street.core.entity.PermissionEntity;
// import com.street.core.filters.JwtRequestFilter;
// import com.street.core.filters.LoggerFilter;
// import com.street.core.repository.PermissionRepo;

// import jakarta.annotation.Resource;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     @Autowired
//     private JwtRequestFilter requestFilter;

//     @Autowired
//     private PermissionRepo permRepo;

//     @Resource
//     private LoggerFilter loggerFilter;

//     @SuppressWarnings("removal")
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         List<PermissionEntity> permissions = StreamSupport.stream(permRepo.findAll().spliterator(), false)
//                 .collect(Collectors.toList());

//         AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry = http
//                 .csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests()
//                 .requestMatchers("/auth/**", "/actuator/**", "/v3/**", "/swagger-*/**", "/images/get/**").permitAll();

        
//         for (PermissionEntity perm : permissions) {
//             registry = registry.requestMatchers(HttpMethod.valueOf(perm.getMethod()), perm.getRoute())
//                     .hasAuthority(perm.getName());
//         }

//         registry
//                 .requestMatchers("/**").authenticated()
//                 .anyRequest().permitAll()
//                 .and()
//                 .sessionManagement(management -> management
//                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }

// }
