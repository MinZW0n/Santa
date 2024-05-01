package com.example.santa.domain.mountain.repository;

import com.example.santa.domain.mountain.dto.MountainResponseDto;
import com.example.santa.domain.mountain.entity.Mountain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MountainRepository extends JpaRepository<Mountain, Long> {

    Page<Mountain> findAll(Pageable pageable);
    //오차범위 계산 공식
    @Query(value = "SELECT m FROM Mountain m WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(m.latitude)) * cos(radians(m.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(m.latitude)))) < :distance ")
    Optional<Mountain> findMountainsWithinDistance(@Param("latitude") double latitude, @Param("longitude") double longitude, @Param("distance") double distance);
}

