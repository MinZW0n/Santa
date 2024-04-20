package com.example.santa.domain.user.repository;

import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

}
