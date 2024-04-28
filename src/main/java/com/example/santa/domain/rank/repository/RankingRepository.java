package com.example.santa.domain.rank.repository;

import com.example.santa.domain.rank.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    Optional<Ranking> findByUserEmail(String userEmail);

    List<Ranking> findAllByOrderByScoreDesc();


}
