package com.example.santa.domain.meeting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class MeetingTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonBackReference("meeting-meetingTag")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @JsonBackReference("tag-meetingTag")
    private Tag tag;

}
