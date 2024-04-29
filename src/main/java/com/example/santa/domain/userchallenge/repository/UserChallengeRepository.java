package com.example.santa.domain.userchallenge.repository;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge,Long> {

//   Optional<UserChallenge> findByUserEmailAndChallengeId(String userEmail, Long challengeId);

//   List<UserChallenge> findByUserAndChallengeCategory(User user, Category category);

   List<UserChallenge> findByUserAndChallenge_Category(User user, Category category);

   Optional<UserChallenge> findByUserAndChallengeId(User user, Long challengeId);


   @Query("SELECT COUNT(uc) FROM UserChallenges uc WHERE uc.user.email = :userEmail AND uc.isCompleted = true")
   Long countCompletedChallengesByUserEmail(String userEmail);


}
