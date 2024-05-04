package com.street.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;



import com.street.core.entity.SessionEntity;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepo  extends JpaRepository<SessionEntity, Long> {
    Optional<SessionEntity> findBySecret(String secret);
    Optional<SessionEntity> findBySecretAndIpAddress(String secretKey, String ipAddress);
}
