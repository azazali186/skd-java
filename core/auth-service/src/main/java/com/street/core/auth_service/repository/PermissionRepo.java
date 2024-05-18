package com.street.core.auth_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.street.core.auth_service.entity.PermissionEntity;

import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<PermissionEntity, Long> {
    PermissionEntity findByName(String name);

}