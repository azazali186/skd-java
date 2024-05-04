package com.street.core.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.street.core.entity.AdminPageEntity;

public interface AdminPageRepo extends JpaRepository<AdminPageEntity, Long> {

    List<AdminPageEntity> findByParentNull();

    @Query("SELECT adminPage FROM AdminPageEntity adminPage WHERE (:name IS NULL OR adminPage.name LIKE %:name%) ")
    Page findAllAdminPage(String name, Pageable pageable);

}