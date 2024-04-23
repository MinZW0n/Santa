package com.example.santa.domain.meeting.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.meeting.dto.MeetingDto;
import com.example.santa.domain.meeting.dto.MeetingResponseDto;
import com.example.santa.domain.meeting.dto.ParticipantDto;
import com.example.santa.domain.meeting.entity.Meeting;
import com.example.santa.domain.meeting.entity.MeetingTag;
import com.example.santa.domain.meeting.entity.Participant;
import com.example.santa.domain.meeting.entity.Tag;
import com.example.santa.domain.meeting.repository.MeetingRepository;
import com.example.santa.domain.meeting.repository.MeetingTagRepository;
import com.example.santa.domain.meeting.repository.ParticipantRepository;
import com.example.santa.domain.meeting.repository.TagRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final MeetingTagRepository meetingTagRepository;
    private final ParticipantRepository participantRepository;

    public MeetingService(MeetingRepository meetingRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository, MeetingTagRepository meetingTagRepository, ParticipantRepository participantRepository) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.meetingTagRepository = meetingTagRepository;
        this.participantRepository = participantRepository;
    }

    public MeetingResponseDto createMeeting(MeetingDto meetingDto){
        Category category = categoryRepository.findByName(meetingDto.getCategoryName())
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
        User leader = userRepository.findByEmail(meetingDto.getUserEmail())
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));


        Meeting meeting = Meeting.builder()
                .meetingName(meetingDto.getMeetingName())
                .category(category)
                .mountainName(meetingDto.getMountainName())
                .description(meetingDto.getDescription())
                .headcount(meetingDto.getHeadcount())
                .date(meetingDto.getDate())
                .image(meetingDto.getImage())
                .build();

        meetingRepository.save(meeting);

        Set<MeetingTag> meetingTags = new HashSet<>();

        for (String tagName : meetingDto.getTags()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder()
                            .name(tagName)
                            .build()));
            MeetingTag meetingTag = MeetingTag.builder()
                    .tag(tag)
                    .meeting(meeting)
                    .build();
            meetingTags.add(meetingTagRepository.save(meetingTag));
        }

        meeting.setMeetingTags(meetingTags);

        meetingRepository.save(meeting);

        Participant participant = Participant.builder()
                .user(leader)
                .meeting(meeting)
                .isLeader(true)
                .build();
        List<Participant> participants = new ArrayList<>();
        participants.add(participantRepository.save(participant));

        meeting.setParticipant(participants);

        return convertToDto(meetingRepository.save(meeting));

    }

    public MeetingResponseDto meetingDetail(Long id){
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.MEETING_NOT_FOUND));
        return convertToDto(meeting);
    }

    public Participant joinMeeting(Long id, String userEmail) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.MEETING_NOT_FOUND));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));

        // 이미 참여중인지 확인
        boolean isAlreadyParticipant = meeting.getParticipant().stream()
                .anyMatch(participant -> participant.getUser().getId().equals(user.getId()));

        if (isAlreadyParticipant) {
            // 이미 참여중인 경우 예외 발생 또는 적절한 처리
            throw new ServiceLogicException(ExceptionCode.ALREADY_PARTICIPATING);
        }

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .isLeader(false)
                .build();
        List<Participant> participants = meeting.getParticipant();
        participants.add(participantRepository.save(participant));

        return participant;
    }

    public List<MeetingResponseDto> getAllMeetings(){

        List<Meeting> meetings = meetingRepository.findAll();
        return meetings.stream().map(this::convertToDto).collect(Collectors.toList());

    }

    @Transactional
    public MeetingResponseDto updateMeeting(Long id, MeetingDto meetingDto) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.MEETING_NOT_FOUND));
        Category category = categoryRepository.findByName(meetingDto.getCategoryName())
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.CATEGORY_NOT_FOUND));

        meeting.setMeetingName(meetingDto.getMeetingName());
        meeting.setCategory(category);
        meeting.setMountainName(meetingDto.getMountainName());
        meeting.setDescription(meetingDto.getDescription());
        meeting.setHeadcount(meetingDto.getHeadcount());
        meeting.setDate(meetingDto.getDate());
        meeting.setImage(meeting.getImage());

        meetingRepository.save(meeting);

        Set<MeetingTag> meetingTags = new HashSet<>();
        meetingTagRepository.deleteByMeeting(meeting);
        for (String tagName : meetingDto.getTags()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder()
                            .name(tagName)
                            .build()));
            MeetingTag meetingTag = MeetingTag.builder()
                    .tag(tag)
                    .meeting(meeting)
                    .build();
            meetingTags.add(meetingTagRepository.save(meetingTag));
        }

        meeting.setMeetingTags(meetingTags);

        return convertToDto(meetingRepository.save(meeting));
    }

    public void deleteMeeting(Long id) {
        if (!meetingRepository.existsById(id)) {
            throw new ServiceLogicException(ExceptionCode.MEETING_NOT_FOUND);
        }
        meetingRepository.deleteById(id);
    }

    public List<MeetingResponseDto> findMeetingsByTagName(String tagName) {
        List<Meeting> meetings = meetingRepository.findByTagName(tagName);
        return meetings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<MeetingResponseDto> getMeetingsByCategoryName(String categoryName) {
        List<Meeting> meetings = meetingRepository.findByCategory_Name(categoryName);
        return meetings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public MeetingResponseDto convertToDto(Meeting meeting) {
        MeetingResponseDto meetingDto = new MeetingResponseDto();
        meetingDto.setMeetingId(meeting.getId());
        meetingDto.setMeetingName(meeting.getMeetingName()); // 모임 이름 설정
        meetingDto.setCategoryName(meeting.getCategory().getName()); // 카테고리 이름 설정
        meetingDto.setMountainName(meeting.getMountainName()); // 산 이름 설정
        meetingDto.setDescription(meeting.getDescription()); // 설명 설정
        meetingDto.setHeadcount(meeting.getHeadcount()); // 인원 수 설정
        meetingDto.setDate(meeting.getDate()); // 날짜 설정
        meetingDto.setImage(meeting.getImage()); // 이미지 설정

        // 태그 설정
        List<String> tags = meeting.getMeetingTags().stream()
                .map(meetingTag -> meetingTag.getTag().getName())
                .collect(Collectors.toList());
        meetingDto.setTags(tags);

        List<Participant> participants = meeting.getParticipant(); // Meeting에서 참가자 목록을 가져옵니다.
        List<ParticipantDto> participantDtoList = new ArrayList<>(); // ParticipantDto 객체를 저장할 리스트를 생성합니다.

        for (Participant participant : participants) {
            User user = participant.getUser(); // 각 Participant에서 User 엔티티를 가져옵니다.

            // ParticipantDto 객체를 생성하고, User 정보를 설정합니다.
            ParticipantDto participantDto = new ParticipantDto();
            participantDto.setUserId(user.getId());
            participantDto.setUserName(user.getName());

            // 생성한 ParticipantDto 객체를 리스트에 추가합니다.
            participantDtoList.add(participantDto);

            // 참가자가 모임장이면 모임장 추가
            if(participant.isLeader()){
                meetingDto.setLeaderId(user.getId());
            }
        }

        // ParticipantDto 리스트를 MeetingDto 객체의 participants로 설정합니다.
        meetingDto.setParticipants(participantDtoList);

        return meetingDto;
    }

}
