package com.example.santa.domain.meeting.service;

import com.example.santa.domain.meeting.dto.MeetingDto;
import com.example.santa.domain.meeting.dto.MeetingResponseDto;
import com.example.santa.domain.meeting.dto.ParticipantDto;
import com.example.santa.domain.meeting.entity.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeetingService {

    MeetingResponseDto createMeeting(MeetingDto meetingDto);
    MeetingResponseDto meetingDetail(Long id);
    Participant joinMeeting(Long id, String userEmail);
    Page<MeetingResponseDto> getAllMeetings(Pageable pageable);
    Page<MeetingResponseDto> getAllMeetingsNoOffset(Long lastId, int size);
    MeetingResponseDto updateMeeting(String email, Long id, MeetingDto meetingDto);
    void deleteMeeting(String email, Long id);
    Page<MeetingResponseDto> getMeetingsByTagName(String tagName, Pageable pageable);
    Page<MeetingResponseDto> getMeetingsByTagNameNoOffset(String tagName, Long lastId, int size);
    Page<MeetingResponseDto> getMeetingsByCategoryName(String categoryName, Pageable pageable);
    Page<MeetingResponseDto> getMeetingsByCategoryNameNoOffset(String categoryName, Long lastId, int size);
    Page<MeetingResponseDto> getAllMeetingsByParticipantCount(Pageable pageable);
    Page<MeetingResponseDto> getAllMeetingsByParticipantCountNoOffset(Long lastId, int size);
    Page<MeetingResponseDto> getMyMeetings(String email, Pageable pageable);
    Page<MeetingResponseDto> getMyMeetingsNoOffset(Long lastId, int size, String email);
    List<ParticipantDto> endMeeting(String email, Long id);
}
