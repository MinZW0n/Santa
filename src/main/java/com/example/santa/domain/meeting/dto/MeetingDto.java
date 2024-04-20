package com.example.santa.domain.meeting.dto;

import com.example.santa.domain.meeting.entity.MeetingTag;
import com.example.santa.domain.meeting.entity.Participant;
import com.example.santa.domain.meeting.entity.Tag;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingDto {
    private Long meetingId;
    private Long leaderId;
    private String meetingName;
    private String categoryName;
    private String mountainName;
    private String description;
    private int headcount;
    private LocalDate date;
    private List<String> tags;
    private String image;
    // 참가자 정보는 모임 상세 조회 시에만 사용
    private List<ParticipantDto> participants;




}
