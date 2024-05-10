package com.example.santa.domain.meeting.controller;

import com.example.santa.domain.meeting.dto.MeetingDto;
import com.example.santa.domain.meeting.dto.MeetingResponseDto;
import com.example.santa.domain.meeting.dto.ParticipantDto;
import com.example.santa.domain.meeting.service.MeetingService;
import com.example.santa.domain.userchallenge.service.UserChallengeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5173/"})
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final UserChallengeServiceImpl userChallengeService;

    public MeetingController(MeetingService meetingService, UserChallengeServiceImpl userChallengeService) {
        this.meetingService = meetingService;
        this.userChallengeService = userChallengeService;
    }

    @Operation(summary = "모임 생성 기능", description = "모임 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @PreAuthorize("hasAuthority('USER') OR hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<MeetingResponseDto> createMeeting(@AuthenticationPrincipal String email, @ModelAttribute @Valid MeetingDto meetingDto){
        return ResponseEntity.ok(meetingService.createMeeting(email, meetingDto));
    }

    @Operation(summary = "모임 조회 기능", description = "모임 id 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDto> meetingDetail(@PathVariable(name = "meetingId") Long id){

        return ResponseEntity.ok(meetingService.meetingDetail(id));

    }

    @Operation(summary = "모임 참여 기능", description = "모임 id로 그 모임 참여")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @PostMapping("{meetingId}/participants")
    public ResponseEntity<?> joinMeeting(@AuthenticationPrincipal String email, @PathVariable(name = "meetingId") Long id){

        meetingService.joinMeeting(id, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "성공적으로 참가되었습니다."));

    }

    @Operation(summary = "모임 조회 기능", description = "모든 모임 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<MeetingResponseDto>> getAllMeetings(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseEntity.ok(meetingService.getAllMeetings(pageRequest));
    }


    @Operation(summary = "모임 수정 기능(모임장)", description = "모임 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @PreAuthorize("hasAuthority('USER') OR hasAuthority('ADMIN')")
    @PatchMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDto> updateMeeting(@AuthenticationPrincipal String email,
            @PathVariable(name = "meetingId") Long id, @ModelAttribute @Valid MeetingDto meetingDto) {
        return ResponseEntity.ok(meetingService.updateMeeting(email, id, meetingDto));
    }

    @Operation(summary = "모임 삭제 기능(모임장,관리자)", description = "모임 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = void.class)))
    })
    @PreAuthorize("hasAuthority('USER') OR hasAuthority('ADMIN')")
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<?> deleteMeeting(@AuthenticationPrincipal String email, @PathVariable(name = "meetingId") Long id) {
        meetingService.deleteMeeting(email,id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모임 검색 기능", description = "모임 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @GetMapping("/tag-search")
    public ResponseEntity<Page<MeetingResponseDto>> getMeetingsByTag(@RequestParam(name = "tag") String tagName,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "5") int size) {
        if (tagName != null) {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
            return ResponseEntity.ok(meetingService.getMeetingsByTagName(tagName,pageRequest));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }



    @Operation(summary = "모임 검색 기능(카테고리)", description = "모임 카테고리 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @GetMapping("/category-search")
    public ResponseEntity<Page<MeetingResponseDto>> getMeetingsByCategoryName(@RequestParam(name = "category") String category,
                                                                              @AuthenticationPrincipal String email,
                                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                                              @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseEntity.ok(meetingService.getMeetingsByCategoryName(email,category,pageRequest));
    }



    @Operation(summary = "모임 조회 기능", description = "모임 인기도순 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @GetMapping("/participants")
    public ResponseEntity<Page<MeetingResponseDto>> getAllMeetingsByParticipantCount(@RequestParam(name = "page", defaultValue = "0") int page,
                                                              @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseEntity.ok(meetingService.getAllMeetingsByParticipantCount(pageRequest));
    }


    @Operation(summary = "모임 조회 기능", description = "내가 참여중인 모임 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @GetMapping("/my-meetings")
    public ResponseEntity<Page<MeetingResponseDto>> getMyMeetings(@AuthenticationPrincipal String email,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseEntity.ok(meetingService.getMyMeetings(email,pageRequest));
    }


    @Operation(summary = "모임 종료 기능(모임장)", description = "모임 종료")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class)))
    })
    @PostMapping("/{meetingId}/end")
    public ResponseEntity<List<ParticipantDto>> endMeeting(@AuthenticationPrincipal String email,
                                        @PathVariable(name = "meetingId") Long id) {

        List<ParticipantDto> participants = meetingService.endMeeting(email, id);

        for (ParticipantDto participant : participants){
            userChallengeService.updateUserChallengeOnMeetingJoin(id, participant.getUserId());
        }

        return ResponseEntity.ok(participants);

    }

}
