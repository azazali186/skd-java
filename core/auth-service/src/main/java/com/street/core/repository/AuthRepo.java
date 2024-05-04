package com.street.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import com.street.core.entity.UserEntity;
import java.util.Optional;

public interface AuthRepo extends JpaRepository<UserEntity, Long> {
        Optional<UserEntity> findByUsername(String username);

        Optional<UserEntity> findByUsernameAndRoleName(String username, String roleName);

        Optional<UserEntity> findByUsernameAndRoleId(String username, Long roleId);

        @Query("SELECT users FROM UserEntity users JOIN users.role role " +
                        "WHERE (:username IS NULL OR users.username LIKE %:username%) AND role.id NOT IN (2, 3)")
        Page findUsers(
                        @Param("username") String username,
                        Pageable pageable);

        @Query("SELECT users FROM UserEntity users JOIN users.role role " +
                        "WHERE (:username IS NULL OR users.username LIKE %:username%) AND role.id IN (2)")
        Page findMembers(
                        @Param("username") String username,
                        Pageable pageable);

        @Query("SELECT users FROM UserEntity users JOIN users.role role " +
                        "WHERE (:username IS NULL OR users.username LIKE %:username%) AND role.id IN (2)")
        Page findVendors(
                        @Param("username") String username,
                        Pageable pageable);
}
