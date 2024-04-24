package com.example.santa.domain.user.repository;

import com.example.santa.domain.meeting.entity.Meeting;
import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.entity.User;
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

}
