package com.example.santa.domain.challege.repository;

import com.example.santa.domain.challege.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    Page<Challenge> findAll(Pageable pageable);
}
