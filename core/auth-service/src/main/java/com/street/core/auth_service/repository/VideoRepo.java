package com.street.core.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



import com.street.core.auth_service.entity.VideoEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepo extends JpaRepository<VideoEntity, Long> {

    Optional<VideoEntity> findByUrl(String video);

}
