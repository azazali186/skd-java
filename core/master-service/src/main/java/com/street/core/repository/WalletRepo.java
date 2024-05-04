package com.street.core.repository;


import com.street.core.entity.WalletEntity;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;


public interface WalletRepo extends JpaRepository<WalletEntity, Long> {

    @Query("SELECT wallet FROM WalletEntity wallet WHERE (:name IS NULL OR wallet.name LIKE %:name%) ")
    Page findAllWallet(@Param("name") String name, Pageable pageable);

    Optional<WalletEntity> findByAddress(String address);
   
}
