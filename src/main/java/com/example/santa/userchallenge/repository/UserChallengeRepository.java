package com.example.santa.userchallenge.repository;

import com.example.santa.userchallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {
}
