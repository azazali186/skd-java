package com.street.core.auth_service.repository;

import com.street.core.auth_service.entity.ImageEntity;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<ImageEntity, Long> {

}
