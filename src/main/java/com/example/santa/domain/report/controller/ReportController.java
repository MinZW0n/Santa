package com.example.santa.domain.report.controller;

import com.example.santa.domain.report.dto.ReportRequestDto;
import com.example.santa.domain.report.dto.ReportResponseDto;
import com.example.santa.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "신고 기능", description = "모임 상세페이지에서 사용자 프로필을 눌러 대상 유저를 신고하는 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ReportResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<ReportResponseDto> reporting(@AuthenticationPrincipal String email, @RequestBody ReportRequestDto reportRequestDto) {

        return ResponseEntity.ok(reportService.reporting(email, reportRequestDto));
    }
    @Operation(summary = "*관리자* 신고 목록 조회 기능", description = "신고 목록 조회 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ReportResponseDto.class)))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<ReportResponseDto>> getReports(@RequestParam(name = "page", defaultValue = "0") int page,
                                                              @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(reportService.getReports(pageRequest));
    }

    @Operation(summary = "*관리자* 신고 목록 삭제 기능", description = "신고 목록 조회 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ReportResponseDto.class)))
    })
    @DeleteMapping("/{reportId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteReport(@PathVariable(name = "reportId") Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }
}
