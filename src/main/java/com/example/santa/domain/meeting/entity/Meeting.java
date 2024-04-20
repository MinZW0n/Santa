package com.example.santa.domain.meeting.entity;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String meetingName;
    private String mountainName;
    private String description;
    private int headcount;
    private LocalDate date;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    private User leader;

    @OneToMany(mappedBy = "meeting")
    @JsonManagedReference("meeting-meetingTag")
    private Set<MeetingTag> meetingTags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name")
    private Category category;

    @OneToMany(mappedBy = "meeting")
    private List<Participant> participant;


}
