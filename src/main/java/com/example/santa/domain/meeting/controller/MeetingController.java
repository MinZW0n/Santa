package com.example.santa.domain.meeting.controller;

import com.example.santa.domain.meeting.dto.MeetingDto;
import com.example.santa.domain.meeting.dto.UserIdDto;
import com.example.santa.domain.meeting.entity.Participant;
import com.example.santa.domain.meeting.service.MeetingService;
import com.example.santa.domain.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public MeetingDto createMeeting(@RequestBody @Valid MeetingDto meetingDto){
        MeetingDto m = new MeetingDto();
        return m = meetingService.createMeeting(meetingDto);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingDto> meetingDetail(@PathVariable(name = "meetingId") Long id){

        return ResponseEntity.ok(meetingService.meetingDetail(id));

    }

    @PostMapping("{meetingId}/participants")
    public ResponseEntity<?> joinMeeting(@PathVariable(name = "meetingId") Long id, @RequestBody UserIdDto user){
        meetingService.joinMeeting(id, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "성공적으로 참가되었습니다."));

    }

    @GetMapping
    public ResponseEntity<?> getAllMeetings(){
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @PatchMapping("/{meetingId}")
    public ResponseEntity<MeetingDto> updateMeeting(@PathVariable(name = "meetingId") Long id, @RequestBody @Valid MeetingDto meetingDto) {
        return ResponseEntity.ok(meetingService.updateMeeting(id, meetingDto));
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<?> deleteMeeting(@PathVariable(name = "meetingId") Long id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tag-search")
    public ResponseEntity<List<MeetingDto>> getMeetingsByTag(@RequestParam(name = "tag") String tagName) {
        if (tagName != null) {
            List<MeetingDto> meetings = meetingService.findMeetingsByTagName(tagName);
            return ResponseEntity.ok(meetings);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category-search")
    public List<MeetingDto> getMeetingsByCategoryName(@RequestParam(name = "category") String category) {
        return meetingService.getMeetingsByCategoryName(category);
    }

}
