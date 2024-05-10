package com.example.santa.domain.meeting.repository;

import com.example.santa.domain.meeting.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Page<Meeting> findByMeetingTags_Tag_NameContaining(String tagName, Pageable pageable);

    Page<Meeting> findByCategory_Name(String categoryName, Pageable pageable);

    @Query("SELECT m FROM Meeting m WHERE m.category IN (SELECT pc.category FROM PreferredCategory pc WHERE pc.user.email = :email)")
    Page<Meeting> findByUserEmailAndPreferredCategories(@Param("email") String userEmail, Pageable pageable);


    @Query("SELECT m FROM Meeting m LEFT JOIN m.participant p WHERE m.date > CURRENT_DATE GROUP BY m.id ORDER BY COUNT(p) DESC")
    Page<Meeting> findAllByParticipantCountAndDateAfterToday(Pageable pageable);


    @Query("SELECT m FROM Meeting m JOIN m.participant p WHERE p.user.id = :userId")
    Page<Meeting> findMeetingsByParticipantUserId(@Param("userId") Long userId, Pageable pageable);


}
