package com.street.core.repository;

import com.street.core.entity.ImageEntity;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<ImageEntity, Long> {

}
