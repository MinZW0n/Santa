package com.example.santa.domain.meeting.repository;

import com.example.santa.domain.meeting.entity.Meeting;
import com.example.santa.domain.meeting.entity.MeetingTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingTagRepository extends JpaRepository<MeetingTag, Long> {
    void deleteByMeeting(Meeting meeting);
}
