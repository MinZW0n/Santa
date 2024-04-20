package com.example.santa.domain.userchallenge.repository;

import com.example.santa.domain.userchallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
}
