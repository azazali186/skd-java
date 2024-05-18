package com.street.core.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;



import com.street.core.auth_service.entity.SessionEntity;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepo  extends JpaRepository<SessionEntity, Long> {
    Optional<SessionEntity> findBySecret(String secret);
    Optional<SessionEntity> findBySecretAndIpAddress(String secretKey, String ipAddress);
}
