package com.example.santa.challege.repository;

import com.example.santa.challege.entity.Challenge;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {

//    Page<Challenge> findChallengeById(Long id, Pageable pageable);
}
