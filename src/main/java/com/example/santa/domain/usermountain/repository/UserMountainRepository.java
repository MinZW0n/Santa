package com.example.santa.domain.usermountain.repository;

import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.usermountain.entity.UserMountain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserMountainRepository extends JpaRepository<UserMountain, Long> {
    Optional<UserMountain> findByUserAndMountainAndClimbDate(User user, Mountain mountain, LocalDate climbDate);
    Optional<UserMountain> findByCategoryName(String categoryName);

}
