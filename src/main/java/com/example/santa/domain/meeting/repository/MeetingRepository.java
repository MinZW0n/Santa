package com.example.santa.domain.meeting.repository;

import com.example.santa.domain.meeting.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m JOIN m.meetingTags mt JOIN mt.tag t WHERE t.name = :tagName")
    Page<Meeting> findByTagName(@Param("tagName") String tagName, Pageable pageable);

    @Query("SELECT m FROM Meeting m JOIN m.meetingTags mt JOIN mt.tag t WHERE t.name = :tagName AND m.id < :lastId")
    Page<Meeting> findByTagNameAndIdLessThan(@Param("tagName") String tagName, @Param("lastId") Long lastId, Pageable pageable);


    Page<Meeting> findByCategory_Name(String categoryName, Pageable pageable);

    Page<Meeting> findByCategory_NameAndIdLessThan(String categoryName, Long lastId, Pageable pageable);

    @Query("SELECT m FROM Meeting m LEFT JOIN m.participant p GROUP BY m.id ORDER BY COUNT(p) DESC")
    Page<Meeting> findAllByParticipantCount(Pageable pageable);

    @Query("SELECT m FROM Meeting m LEFT JOIN m.participant p GROUP BY m.id HAVING m.id < :lastId ORDER BY COUNT(p) DESC, m.id DESC")
    Page<Meeting> findAllByParticipantCountAndIdLessThan(@Param("lastId") Long lastId, Pageable pageable);

    @Query("SELECT m FROM Meeting m JOIN m.participant p WHERE p.user.id = :userId")
    Page<Meeting> findMeetingsByParticipantUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT m FROM Meeting m JOIN m.participant p WHERE p.user.id = :userId AND m.id < :lastId ORDER BY m.id DESC")
    Page<Meeting> findMeetingsByParticipantUserIdAndIdLessThan(@Param("userId") Long userId, @Param("lastId") Long lastId, Pageable pageable);



    Page<Meeting> findByIdLessThanOrderByIdDesc(Long lastId, Pageable pageable);
}
