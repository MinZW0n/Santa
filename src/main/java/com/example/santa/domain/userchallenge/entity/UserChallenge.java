package com.example.santa.domain.userchallenge.entity;

import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "UserChallenges")
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer progress;

    @Column
    private Boolean isCompleted;

    @Column
    private LocalDate completionDate;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

