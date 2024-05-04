package com.street.core.repository;


import com.street.core.entity.CurrencyEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

public interface CurrencyRepo extends JpaRepository<CurrencyEntity, Long> {

    @Query("SELECT currency FROM CurrencyEntity currency WHERE (:name IS NULL OR currency.name LIKE %:name%) ")
    Page findAllCurrency(@Param("name") String name, Pageable pageable);
   
}
