package com.example.santa.domain.user.repository;

import com.example.santa.domain.meeting.entity.Meeting;
import com.example.santa.domain.user.dto.UserReportResponseDto;
import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.entity.SocialType;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import com.example.santa.domain.usermountain.entity.UserMountain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    @Query("SELECT m FROM Meeting m JOIN m.participant p WHERE p.user.id = :userId")
    List<Meeting> findMeetingsByUserId(@Param("userId") Long userId);

    @Query("SELECT um FROM UserMountain um WHERE um.user.id = :userId")
    Page<UserMountain> findUserMountainsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT uc FROM UserChallenges uc WHERE uc.user.id = :userid AND uc.isCompleted = true")
    Page<UserChallenge> findByUserIdAndIsCompletedTrue(Long userid,Pageable pageable);

    @Query("SELECT uc FROM UserChallenges uc WHERE uc.user.id = :userid AND uc.isCompleted IS NULL")
    Page<UserChallenge> findByUserIdAndIsCompletedNull(Long userid,Pageable pageable);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    //    Page<User> findAllByNameContainingAndNicknameContaining (String name, String nickname, Pageable pageable);
    Page<User> findAllByNameContainingOrNicknameContaining (String name, String nickname, Pageable pageable);

    @Query("SELECT new com.example.santa.domain.user.dto.UserReportResponseDto(u.id, u.email, u.nickname, u.name, COUNT(r)) FROM User u LEFT JOIN Report r ON u = r.reportedParticipant WHERE (:search IS NULL OR u.name LIKE %:search% OR u.nickname LIKE %:search%) GROUP BY u.id, u.email, u.nickname, u.name")
    Page<UserReportResponseDto> findUsersWithReportCount(@Param("search") String search, Pageable pageable);
}
