package com.street.core.master_service.repository;

import com.street.core.master_service.entity.ImageEntity;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<ImageEntity, Long> {

}
