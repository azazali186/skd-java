package com.street.core.master_service.repository;


import com.street.core.master_service.entity.NetworkEntity;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;


public interface NetworkRepo extends JpaRepository<NetworkEntity, Long> {

    @Query("SELECT network FROM NetworkEntity network WHERE (:name IS NULL OR network.name LIKE %:name%) ")
    Page findAllNetwork(@Param("name") String name, Pageable pageable);

    Optional<NetworkEntity> findByName(String name);

    Optional<NetworkEntity> findByCode(String string);
   
}
