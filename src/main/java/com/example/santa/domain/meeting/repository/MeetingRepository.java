package com.example.santa.domain.meeting.repository;

import com.example.santa.domain.meeting.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m JOIN m.meetingTags mt JOIN mt.tag t WHERE t.name = :tagName")
    List<Meeting> findByTagName(@Param("tagName") String tagName);

    List<Meeting> findByCategory_Name(String categoryName);
}
