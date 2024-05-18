package com.street.core.master_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import com.street.core.master_service.entity.UserEntity;
import java.util.Optional;

public interface AuthRepo extends JpaRepository<UserEntity, Long> {

        
}
