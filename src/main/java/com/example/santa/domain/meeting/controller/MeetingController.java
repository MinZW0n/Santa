package com.example.santa.domain.meeting.controller;

import com.example.santa.domain.meeting.dto.MeetingDto;
import com.example.santa.domain.meeting.dto.MeetingResponseDto;
import com.example.santa.domain.meeting.dto.UserIdDto;
import com.example.santa.domain.meeting.service.MeetingService;
import com.example.santa.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final JwtTokenProvider jwt;

    public MeetingController(MeetingService meetingService, JwtTokenProvider jwt) {
        this.meetingService = meetingService;
        this.jwt = jwt;
    }

    @PostMapping
    public MeetingResponseDto createMeeting(HttpServletRequest request, @RequestBody @Valid MeetingDto meetingDto){
        String token = jwt.extractToken(request);
        Authentication authentication = jwt.getAuthentication(token);
        meetingDto.setUserEmail(authentication.getName());
        return meetingService.createMeeting(meetingDto);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDto> meetingDetail(@PathVariable(name = "meetingId") Long id){

        return ResponseEntity.ok(meetingService.meetingDetail(id));

    }

    @PostMapping("{meetingId}/participants")
    public ResponseEntity<?> joinMeeting(@PathVariable(name = "meetingId") Long id, HttpServletRequest request){
        String token = jwt.extractToken(request);
        Authentication authentication = jwt.getAuthentication(token);

        meetingService.joinMeeting(id, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "성공적으로 참가되었습니다."));

    }

    @GetMapping
    public ResponseEntity<?> getAllMeetings(){
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @PatchMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDto> updateMeeting(@PathVariable(name = "meetingId") Long id, @RequestBody @Valid MeetingDto meetingDto) {
        return ResponseEntity.ok(meetingService.updateMeeting(id, meetingDto));
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<?> deleteMeeting(@PathVariable(name = "meetingId") Long id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tag-search")
    public ResponseEntity<List<MeetingResponseDto>> getMeetingsByTag(@RequestParam(name = "tag") String tagName) {
        if (tagName != null) {
            List<MeetingResponseDto> meetings = meetingService.findMeetingsByTagName(tagName);
            return ResponseEntity.ok(meetings);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category-search")
    public List<MeetingResponseDto> getMeetingsByCategoryName(@RequestParam(name = "category") String category) {
        return meetingService.getMeetingsByCategoryName(category);
    }

}
