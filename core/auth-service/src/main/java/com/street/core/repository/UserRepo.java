package com.street.core.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.street.core.entity.response.UserEntityResponse;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntityResponse, Long> {
    Optional<UserEntityResponse> findByUsername(String username);

    @Query("SELECT users FROM UserEntityResponse users " +
            "WHERE (:username IS NULL OR users.username LIKE %:username%) ")
    List<UserEntityResponse> findUsers(
            @Param("username") String username,
            Pageable pageable);

    @Query("SELECT COUNT(users) FROM UserEntityResponse users " +
            "WHERE (:username IS NULL OR users.username LIKE %:username%) ")
    Integer countUsers(String username);

}
