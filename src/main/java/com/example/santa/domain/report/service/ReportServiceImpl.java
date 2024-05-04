package com.example.santa.domain.report.service;

import com.example.santa.domain.report.dto.ReportRequestDto;
import com.example.santa.domain.report.dto.ReportResponseDto;
import com.example.santa.domain.report.entity.Report;
import com.example.santa.domain.report.repository.ReportRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.mapsturct.ReportResponseDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportResponseDtoMapper reportResponseDtoMapper;

    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository, ReportResponseDtoMapper reportResponseDtoMapper) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.reportResponseDtoMapper = reportResponseDtoMapper;
    }

    @Override
    public ReportResponseDto reporting(String email, ReportRequestDto reportRequestDto) {

        User reporter = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        User reportedParticipant = userRepository.findById(reportRequestDto.getReportedParticipantId())
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));


        // 중복 신고 확인
        if (reportRepository.existsByReporterAndReportedParticipant(reporter, reportedParticipant)) {
            throw new ServiceLogicException(ExceptionCode.REPORT_ALREADY_EXISTS);
        }

        Report report = Report.builder()
                .reason(reportRequestDto.getReason())
                .reportedParticipant(reportedParticipant)
                .reporter(reporter)
                .build();

        reportRepository.save(report);

        return reportResponseDtoMapper.toDto(report);

    }

    @Override
    public Page<ReportResponseDto> getReports(PageRequest pageRequest) {
        return reportRepository.findAll(pageRequest).map(reportResponseDtoMapper::toDto);
    }

    @Override
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

}
