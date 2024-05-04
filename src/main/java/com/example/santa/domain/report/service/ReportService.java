package com.example.santa.domain.report.service;

import com.example.santa.domain.report.dto.ReportRequestDto;
import com.example.santa.domain.report.dto.ReportResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ReportService {
    ReportResponseDto reporting(String email, ReportRequestDto reportRequestDto);
    Page<ReportResponseDto> getReports(PageRequest pageRequest);
    void deleteReport(Long id);
}
