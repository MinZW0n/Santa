package com.example.santa.domain.usermountain.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.mountain.dto.UserClimbMountainResponseDto;
import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.mountain.repository.MountainRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.service.UserChallengeService;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyRequestDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.repository.UserMountainRepository;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.mapsturct.UserMountainResponseDtoMapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserMountainServiceImplTest {

    @Mock
    private UserMountainRepository userMountainRepository;
    @Mock
    private MountainRepository mountainRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserMountainResponseDtoMapper userMountainResponseDtoMapper;
    @Mock
    private UserChallengeService userChallengeService;

    @InjectMocks
    private UserMountainServiceImpl userMountainService;

    private User user;
    private Mountain mountain;
    private Category category;
    private UserMountain userMountain;
    private UserMountainResponseDto responseDto;
    private UserClimbMountainResponseDto userClimbMountainResponseDto;
    private UserMountainVerifyRequestDto requestDto;

    @BeforeEach
    void setUp() {
        // 객체 초기화
        user = new User();
        user.setEmail("test@email.com");

        mountain = new Mountain();
        mountain.setDescription("test");
        mountain.setId(1L);
        mountain.setLongitude(127.145);
        mountain.setLatitude(123.456);
        mountain.setName("관악산");
        mountain.setHeight(1045.1);
        mountain.setPoint("test");
        mountain.setTransportation("지하철");
        mountain.setLocation("서울시 관악구");

        category = new Category();
        category.setId(1L);
        category.setName("기타");

        userMountain = new UserMountain();
        userMountain.setMountain(mountain);

        responseDto = new UserMountainResponseDto();
        responseDto.setClimbDate(LocalDate.now());
        responseDto.setMountainHeight(1000.1);
        responseDto.setMountainName("관악산");
        responseDto.setMountainLocation("서울시 관악구");
        responseDto.setId(1L);


        requestDto = new UserMountainVerifyRequestDto();
        requestDto.setClimbDate(LocalDate.now());
        requestDto.setLatitude(127.960749);
        requestDto.setLongitude(37.874149);
    }

    @Test
    void verifyAndCreateUserMountain_WhenUserExistsAndMountainExists_ShouldCreateUserMountain() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(categoryRepository.findByName("기타")).thenReturn(Optional.of(category));
        when(mountainRepository.findMountainsWithinDistance(any(Double.class), any(Double.class), any(Double.class)))
                .thenReturn(Optional.of(mountain));
        when(userMountainRepository.findByUserAndMountainAndClimbDate(any(User.class), any(Mountain.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(userMountainRepository.save(any(UserMountain.class))).thenReturn(userMountain);
        when(userMountainResponseDtoMapper.toDto(any(UserMountain.class))).thenReturn(responseDto);
        UserMountainResponseDto result = userMountainService.verifyAndCreateUserMountain(requestDto, user.getEmail());
        //chan@email.com으로 넣어도 테스트가 통과됩니다.
        assertNotNull(user);
        assertNotNull(category);
        assertNotNull(mountain);

        assertEquals(result.getClimbDate(), requestDto.getClimbDate());
        assertEquals(responseDto, result);

//        assertAll(
//                () -> assertNotNull(study),
//                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), “스터디를 처음 만들면 상태값이 DRAFT여야 한다.”),
//                () -> assertTrue(study.getLimit() > 0, “스터디 참석 가능 인원은 0보다 커야 한다.”)
//        );
    }

    @Test
    void verifyAndCreateUserMountain_WhenUserAlreadyClimbedMountainOnSameDate_ShouldThrowException() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(mountainRepository.findMountainsWithinDistance(any(Double.class), any(Double.class), any(Double.class)))
                .thenReturn(Optional.of(mountain));
        when(categoryRepository.findByName("기타")).thenReturn(Optional.of(category));
        when(userMountainRepository.findByUserAndMountainAndClimbDate(any(User.class), any(Mountain.class), any(LocalDate.class)))
                .thenReturn(Optional.of(userMountain));

        Exception exception = assertThrows(ServiceLogicException.class, () -> {
            userMountainService.verifyAndCreateUserMountain(requestDto, user.getEmail());
        });

        assertEquals("이미 같은 날에 이 산에 대한 인증이 존재합니다.", exception.getMessage());
    }

    @Test
    void verifyAndCreateUserMountain_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ServiceLogicException.class, () -> {
            userMountainService.verifyAndCreateUserMountain(requestDto, user.getEmail());
        });

        assertEquals("존재하지 않는 회원입니다.", exception.getMessage());
    }

    @Test
    void verifyAndCreateUserMountain_WhenCategoryDoesNotExist_ShouldThrowException() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
//        when(mountainRepository.findMountainsWithinDistance(any(Double.class), any(Double.class), any(Double.class)))
//                .thenReturn(Optional.of(mountain));
        when(categoryRepository.findByName("기타")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ServiceLogicException.class, () -> {
            userMountainService.verifyAndCreateUserMountain(requestDto, user.getEmail());
        });

        assertEquals("존재하지 않는 카테고리이름 입니다.", exception.getMessage());
    }
}





