package com.example.santa.challege.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Entity
public class Challenge {

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
