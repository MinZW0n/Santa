package com.example.santa.domain.challege.entity;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.common.BaseEntity;
import com.example.santa.domain.meeting.entity.MeetingTag;
import com.example.santa.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Entity
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private  String name;

    @Column
    private String description;

    @Column
    private String image;

    @Column
    private Integer clearStandard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


}
