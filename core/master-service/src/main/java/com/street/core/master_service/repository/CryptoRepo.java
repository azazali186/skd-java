package com.street.core.master_service.repository;


import com.street.core.master_service.entity.CryptoEntity;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;


public interface CryptoRepo extends JpaRepository<CryptoEntity, Long> {

    @Query("SELECT currency FROM CryptoEntity currency WHERE (:name IS NULL OR currency.name LIKE %:name%) ")
    Page findAllCrypto(@Param("name") String name, Pageable pageable);

    Optional<CryptoEntity> findByContractAndSymbol(String contract, String symbol);

    CryptoEntity findByContract(String contractAddress);
   
}
