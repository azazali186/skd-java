package com.street.core.repository;

import com.street.core.entity.WhiteListIpEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

@Repository
public interface WhiteListIPRepo extends JpaRepository<WhiteListIpEntity, Long> {

    @Query("SELECT w FROM WhiteListIpEntity w WHERE (:ipAddress IS NULL OR w.ipAddress LIKE %:ipAddress%) ")
    Page findAllIpList(String ipAddress, Pageable pageable);

}
