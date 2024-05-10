package com.example.santa.domain.meeting.service;

import com.example.santa.domain.meeting.dto.MeetingDto;
import com.example.santa.domain.meeting.dto.MeetingResponseDto;
import com.example.santa.domain.meeting.dto.ParticipantDto;
import com.example.santa.domain.meeting.entity.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeetingService {

    MeetingResponseDto createMeeting(String email, MeetingDto meetingDto);
    MeetingResponseDto meetingDetail(Long id);
    Participant joinMeeting(Long id, String userEmail);
    Page<MeetingResponseDto> getAllMeetings(Pageable pageable);
    MeetingResponseDto updateMeeting(String email, Long id, MeetingDto meetingDto);
    void deleteMeeting(String email, Long id);
    Page<MeetingResponseDto> getMeetingsByTagName(String tagName, Pageable pageable);
    Page<MeetingResponseDto> getMeetingsByCategoryName(String email, String categoryName, Pageable pageable);
    Page<MeetingResponseDto> getAllMeetingsByParticipantCount(Pageable pageable);
    Page<MeetingResponseDto> getMyMeetings(String email, Pageable pageable);
    List<ParticipantDto> endMeeting(String email, Long id);
}
