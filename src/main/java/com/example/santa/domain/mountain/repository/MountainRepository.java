package com.example.santa.domain.mountain.repository;

import com.example.santa.domain.mountain.entity.Mountain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MountainRepository extends JpaRepository<Mountain, Long> {
}
