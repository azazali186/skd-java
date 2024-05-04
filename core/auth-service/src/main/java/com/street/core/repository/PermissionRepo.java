package com.street.core.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.street.core.entity.PermissionEntity;

import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<PermissionEntity, Long> {
    PermissionEntity findByName(String name);

}