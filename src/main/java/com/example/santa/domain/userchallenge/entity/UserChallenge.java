package com.example.santa.domain.userchallenge.entity;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.usermountain.entity.UserMountain;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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

//    @ManyToOne
//    @JoinColumn(name = "userMountain_id")
//    private UserMountain userMountain;

//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;

}
