package com.example.santa.domain.meeting.repository;

import com.example.santa.domain.meeting.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
