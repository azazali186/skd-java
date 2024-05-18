package com.street.core.master_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import com.street.core.master_service.entity.LoggerEntity;

@Repository
public interface LoggerRepo extends JpaRepository<LoggerEntity, Long> {
}
