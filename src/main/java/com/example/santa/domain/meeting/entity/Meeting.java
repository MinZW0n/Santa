package com.example.santa.domain.meeting.entity;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.common.BaseEntity;
import com.example.santa.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "모임 이름을 입력하세요.")
    private String meetingName;
    private String mountainName;
    @NotBlank(message = "상세 설명을 입력하세요.")
    private String description;
    @NotNull(message = "인원 수를 입력하세요.")
    private int headcount;
    @NotNull(message = "날짜를 입력하세요.")
    private LocalDate date;
    private String image;
    private boolean end = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User leader;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("meeting-meetingTag")
    private Set<MeetingTag> meetingTags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name")
    private Category category;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participant;


}
