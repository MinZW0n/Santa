package com.example.santa.domain.userchallenge.entity;

import com.example.santa.domain.challege.entity.Challenge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "UserChallenges")
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String progress;

    @Column
    private Boolean isCompleted;

    @Column
    private Date completionDate;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;


}
