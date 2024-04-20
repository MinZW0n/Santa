package com.example.santa.domain.challege.entity;

import com.example.santa.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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



}
