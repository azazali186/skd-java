package com.street.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.street.core.entity.RoleEntity;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo  extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findById(Integer id);

    @Query("SELECT r FROM RoleEntity r LEFT JOIN FETCH r.permissions WHERE r.id = :id")
    Optional<RoleEntity> findByIdWithPermissions(@Param("id") Long id);
}
