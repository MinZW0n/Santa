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
import com.example.santa.domain.user.entity.Role;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.S3ImageService;
import com.example.santa.global.util.mapsturct.ParticipantsDtoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final MeetingTagRepository meetingTagRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantsDtoMapper participantsDtoMapper;
    private final S3ImageService s3ImageService;

    public MeetingService(MeetingRepository meetingRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository, MeetingTagRepository meetingTagRepository, ParticipantRepository participantRepository, ParticipantsDtoMapper participantsDtoMapper, S3ImageService s3ImageService) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.meetingTagRepository = meetingTagRepository;
        this.participantRepository = participantRepository;
        this.participantsDtoMapper = participantsDtoMapper;
        this.s3ImageService = s3ImageService;
    }

    public MeetingResponseDto createMeeting(MeetingDto meetingDto){
        // dto에서 불러온 카테고리명으로 카테고리를 가져옴
        Category category = categoryRepository.findByName(meetingDto.getCategoryName())
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
        // 현재 로그인 한 유저를 불러옴
        User leader = userRepository.findByEmail(meetingDto.getUserEmail())
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));

        // 이미 같은 날짜에 다른 모임에 참여 중인지 확인
        boolean isParticipatingOnSameDate = userRepository.findMeetingsByUserId(leader.getId()).stream()
                .anyMatch(m -> m.getDate().equals(meetingDto.getDate()));

        if (isParticipatingOnSameDate) {
            // 같은 날짜에 다른 모임에 이미 참여중인 경우 예외 발생
            throw new ServiceLogicException(ExceptionCode.ALREADY_PARTICIPATING_ON_DATE);
        }

        MultipartFile imageFile = meetingDto.getImageFile();
        String imageUrl = "defaultUrl";
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3ImageService.upload(imageFile);
        }


        Meeting meeting = Meeting.builder()
                .meetingName(meetingDto.getMeetingName())
                .leader(leader)
                .category(category)
                .mountainName(meetingDto.getMountainName())
                .description(meetingDto.getDescription())
                .headcount(meetingDto.getHeadcount())
                .date(meetingDto.getDate())
                .image(imageUrl)
                .build();

        meetingRepository.save(meeting);

        Set<MeetingTag> meetingTags = new HashSet<>();
        // dto에 있는 해시태그에서 생성되지 않은 해시태그면 생성해주고 생성되어 있으면 가져옴
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
        // 모임장을 참가자 목록에 추가해줌
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

        // 이미 같은 날짜에 다른 모임에 참여 중인지 확인
        boolean isParticipatingOnSameDate = userRepository.findMeetingsByUserId(user.getId()).stream()
                .anyMatch(m -> m.getDate().equals(meeting.getDate()));

        if (isParticipatingOnSameDate) {
            // 같은 날짜에 다른 모임에 이미 참여중인 경우 예외 발생
            throw new ServiceLogicException(ExceptionCode.ALREADY_PARTICIPATING_ON_DATE);
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

    public Page<MeetingResponseDto> getAllMeetings(Pageable pageable){

        Page<Meeting> meetings = meetingRepository.findAll(pageable);
        return meetings.map(this::convertToDto);

    }
    public Page<MeetingResponseDto> getAllMeetingsNoOffset(Long lastId, int size) {
        Page<Meeting> meetings;
        if (lastId == null) {
            // 커서가 제공되지 않은 경우 처음부터 조회
            meetings = meetingRepository.findAll(PageRequest.of(0, size, Sort.by("id").descending()));
        } else {
            // 커서를 기반으로 다음 데이터 조회
            meetings = meetingRepository.findByIdLessThanOrderByIdDesc(lastId, PageRequest.of(0, size));
        }
        return meetings.map(this::convertToDto);
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

    public void deleteMeeting(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.MEETING_NOT_FOUND));

        if (!meetingRepository.existsById(id)) {
            throw new ServiceLogicException(ExceptionCode.MEETING_NOT_FOUND);
        }
        if (!Objects.equals(user.getId(), meeting.getLeader().getId()) || user.getRole() == Role.ADMIN){
            throw new ServiceLogicException(ExceptionCode.USER_NOT_LEADER);
        }
        meetingRepository.deleteById(id);
    }

    public Page<MeetingResponseDto> getMeetingsByTagName(String tagName, Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.findByTagName(tagName,pageable);
        return meetings.map(this::convertToDto);
    }

    public Page<MeetingResponseDto> getMeetingsByTagNameNoOffset(String tagName, Long lastId, int size) {
        Page<Meeting> meetings;
        if (lastId == null) {
            meetings = meetingRepository.findByTagName(tagName, PageRequest.of(0, size, Sort.by("id").descending()));
        } else {
            meetings = meetingRepository.findByTagNameAndIdLessThan(tagName, lastId, PageRequest.of(0, size, Sort.by("id").descending()));
        }
        return meetings.map(this::convertToDto);
    }


    public Page<MeetingResponseDto> getMeetingsByCategoryName(String categoryName, Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.findByCategory_Name(categoryName,pageable);
        return meetings.map(this::convertToDto);
    }

    public Page<MeetingResponseDto> getMeetingsByCategoryNameNoOffset(String categoryName, Long lastId, int size) {
        Page<Meeting> meetings;
        if (lastId == null) {
            // lastId가 null일 경우, 가장 최근 데이터부터 시작
            meetings = meetingRepository.findByCategory_Name(categoryName, PageRequest.of(0, size, Sort.by("id").descending()));
        } else {
            // lastId를 기반으로 다음 데이터 조회
            meetings = meetingRepository.findByCategory_NameAndIdLessThan(categoryName, lastId, PageRequest.of(0, size, Sort.by("id").descending()));
        }
        return meetings.map(this::convertToDto);
    }


    public Page<MeetingResponseDto> getAllMeetingsByParticipantCount(Pageable pageable) {
        Page<Meeting> meetings = meetingRepository.findAllByParticipantCount(pageable);
        return meetings.map(this::convertToDto);
    }

    public Page<MeetingResponseDto> getAllMeetingsByParticipantCountNoOffset(Long lastId, int size) {
        Page<Meeting> meetings;
        if (lastId == null) {
            // lastId가 제공되지 않은 경우, 가장 최근 데이터부터 시작
            meetings = meetingRepository.findAllByParticipantCount(PageRequest.of(0, size, Sort.by("id").descending()));
        } else {
            meetings = meetingRepository.findAllByParticipantCountAndIdLessThan(lastId, PageRequest.of(0, size, Sort.by("id").descending()));
        }
        return meetings.map(this::convertToDto);
    }


    public Page<MeetingResponseDto> getMyMeetings(String email, Pageable pageable){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));

        Page<Meeting> meetings = meetingRepository.findMeetingsByParticipantUserId(user.getId(), pageable);
        return meetings.map(this::convertToDto);
    }

    public Page<MeetingResponseDto> getMyMeetingsNoOffset(Long lastId, int size, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        Page<Meeting> meetings;
        if (lastId == null) {
            // lastId가 제공되지 않은 경우, 가장 최근 데이터부터 시작
            meetings = meetingRepository.findMeetingsByParticipantUserId(user.getId(), PageRequest.of(0, size, Sort.by("id").descending()));
        } else {
            meetings = meetingRepository.findMeetingsByParticipantUserIdAndIdLessThan(user.getId(),lastId, PageRequest.of(0, size, Sort.by("id").descending()));
        }
        return meetings.map(this::convertToDto);
    }

    public List<ParticipantDto> endMeeting(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.MEETING_NOT_FOUND));

//        meeting.setEnd(true);
//
//        meetingRepository.save(meeting);

        return participantsDtoMapper.toDtoList(meeting.getParticipant());

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
            participantDto.setUserImage(user.getImage());

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
