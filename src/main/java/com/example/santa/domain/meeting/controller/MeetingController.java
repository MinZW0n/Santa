package com.example.santa.domain.meeting.controller;

import com.example.santa.domain.meeting.dto.MeetingDto;
import com.example.santa.domain.meeting.dto.MeetingResponseDto;
import com.example.santa.domain.meeting.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5173/"})
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public MeetingResponseDto createMeeting(@AuthenticationPrincipal String email, @RequestBody @Valid MeetingDto meetingDto){
        meetingDto.setUserEmail(email);
        return meetingService.createMeeting(meetingDto);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDto> meetingDetail(@PathVariable(name = "meetingId") Long id){

        return ResponseEntity.ok(meetingService.meetingDetail(id));

    }

    @PostMapping("{meetingId}/participants")
    public ResponseEntity<?> joinMeeting(@AuthenticationPrincipal String email, @PathVariable(name = "meetingId") Long id){

        meetingService.joinMeeting(id, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "성공적으로 참가되었습니다."));

    }

//    @GetMapping
//    public ResponseEntity<?> getAllMeetings(@RequestParam(name = "page", defaultValue = "0") int page,
//                                            @RequestParam(name = "size", defaultValue = "10") int size) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
//        return ResponseEntity.ok(meetingService.getAllMeetings(pageRequest));
//    }

    @GetMapping
    public ResponseEntity<?> getAllMeetings(@RequestParam(name = "lastId", required = false) Long lastId,
                                            @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(meetingService.getAllMeetingsNoOffset(lastId, size));
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

//    @GetMapping("/tag-search")
//    public ResponseEntity<?> getMeetingsByTag(@RequestParam(name = "tag") String tagName,
//                                              @RequestParam(name = "page", defaultValue = "0") int page,
//                                              @RequestParam(name = "size", defaultValue = "10") int size) {
//        if (tagName != null) {
//            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
//            return ResponseEntity.ok(meetingService.getMeetingsByTagName(tagName,pageRequest));
//        } else {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @GetMapping("/tag-search")
    public ResponseEntity<?> getMeetingsByTag(@RequestParam(name = "tag") String tagName,
                                              @RequestParam(name = "lastId", required = false) Long lastId,
                                              @RequestParam(name = "size", defaultValue = "5") int size) {
        if (tagName != null) {
            return ResponseEntity.ok(meetingService.getMeetingsByTagNameNoOffset(tagName, lastId, size));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


//    @GetMapping("/category-search")
//    public ResponseEntity<?> getMeetingsByCategoryName(@RequestParam(name = "category") String category,
//                                                              @RequestParam(name = "page", defaultValue = "0") int page,
//                                                              @RequestParam(name = "size", defaultValue = "10") int size) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
//        return ResponseEntity.ok(meetingService.getMeetingsByCategoryName(category,pageRequest));
//    }

    @GetMapping("/category-search")
    public ResponseEntity<?> getMeetingsByCategoryName(@RequestParam(name = "category") String category,
                                                       @RequestParam(name = "lastId", required = false) Long lastId,
                                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        if (category != null) {
            return ResponseEntity.ok(meetingService.getMeetingsByCategoryNameNoOffset(category, lastId, size));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

//    @GetMapping("/participants")
//    public ResponseEntity<?> getAllMeetingsByParticipantCount(@RequestParam(name = "page", defaultValue = "0") int page,
//                                                              @RequestParam(name = "size", defaultValue = "10") int size) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
//        return ResponseEntity.ok(meetingService.getAllMeetingsByParticipantCount(pageRequest));
//    }

    @GetMapping("/participants")
    public ResponseEntity<?> getAllMeetingsByParticipantCount(@RequestParam(name = "lastId", required = false) Long lastId,
                                                              @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(meetingService.getAllMeetingsByParticipantCountNoOffset(lastId, size));
    }


//    @GetMapping("/my-meetings")
//    public ResponseEntity<?> getMyMeetings(@AuthenticationPrincipal String email,
//                                           @RequestParam(name = "page", defaultValue = "0") int page,
//                                           @RequestParam(name = "size", defaultValue = "5") int size) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
//        return ResponseEntity.ok(meetingService.getMyMeetings(email,pageRequest));
//    }

    @GetMapping("/my-meetings")
    public ResponseEntity<?> getMyMeetings(@AuthenticationPrincipal String email,
                                           @RequestParam(name = "lastId", required = false) Long lastId,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(meetingService.getMyMeetingsNoOffset(lastId, size, email));
    }


}
