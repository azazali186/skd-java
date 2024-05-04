package com.street.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import com.street.core.entity.LoggerEntity;

@Repository
public interface LoggerRepo extends JpaRepository<LoggerEntity, Long> {
}
