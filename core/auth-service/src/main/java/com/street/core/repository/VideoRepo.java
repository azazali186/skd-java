package com.street.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



import com.street.core.entity.VideoEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepo extends JpaRepository<VideoEntity, Long> {

    Optional<VideoEntity> findByUrl(String video);

}
