package com.example.santa.domain.usermountain.entity;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.common.BaseEntity;
import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Usermountains")
public class UserMountain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate climbDate;
    private double latitude;
    private double longitude;
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name")
    private Category category;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userChallenge_id")
//    private UserChallenge userChallenge;


}
