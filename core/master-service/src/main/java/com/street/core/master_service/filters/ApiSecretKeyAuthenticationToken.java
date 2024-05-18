// package com.street.core.master_service.filters;

// import java.util.Collection;

// import org.springframework.security.authentication.AbstractAuthenticationToken;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.Transient;

// @Transient
// public class ApiSecretKeyAuthenticationToken extends AbstractAuthenticationToken {

//     private String apiKey;
    
//     public ApiSecretKeyAuthenticationToken(String apiKey, Collection<? extends GrantedAuthority> authorities) {
//         super(authorities);
//         this.apiKey = apiKey;
//         setAuthenticated(true);
//     }

//     @Override
//     public Object getCredentials() {
//         return null;
//     }

//     @Override
//     public Object getPrincipal() {
//         return apiKey;
//     }
// }
