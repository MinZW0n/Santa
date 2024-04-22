package com.example.santa.domain.meeting.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "태그를 입력하세요.")
    private String name;

    @OneToMany(mappedBy = "tag")
    @JsonManagedReference("tag-meetingTag")
    private Set<MeetingTag> meetingTags = new HashSet<>();


}
