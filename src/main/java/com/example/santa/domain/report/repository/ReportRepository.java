package com.example.santa.domain.report.repository;

import com.example.santa.domain.report.entity.Report;
import com.example.santa.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findAll(Pageable pageable);
    // 특정 신고자와 특정 참가자에 대한 신고가 있는지 확인하는 쿼리 메소드
    boolean existsByReporterAndReportedParticipant(User reporter, User reportedParticipant);
}
