package com.example.santa.domain.meeting.controller;

import com.example.santa.domain.meeting.dto.MeetingDto;
import com.example.santa.domain.meeting.entity.Meeting;
import com.example.santa.domain.meeting.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public MeetingDto createMeeting(@RequestBody MeetingDto meetingDto){
        MeetingDto m = new MeetingDto();
        return m = meetingService.createMeeting(meetingDto);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingDto> meetingDetail(@PathVariable(name = "meetingId") Long id){

        return ResponseEntity.ok(meetingService.meetingDetail(id));

    }
}
