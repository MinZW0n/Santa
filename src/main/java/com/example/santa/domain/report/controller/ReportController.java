package com.example.santa.domain.report.controller;

import com.example.santa.domain.report.dto.ReportRequestDto;
import com.example.santa.domain.report.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<?> reporting(@AuthenticationPrincipal String email, @RequestBody ReportRequestDto reportRequestDto) {

        return ResponseEntity.ok(reportService.reporting(email, reportRequestDto));
    }

    @GetMapping
    public ResponseEntity<?> getReports(@RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(reportService.getReports(pageRequest));
    }
}
