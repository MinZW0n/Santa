package com.example.santa.domain.challege.service;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.challege.dto.ChallengeCreateDto;
import com.example.santa.domain.challege.dto.ChallengeParticipationResponseDto;
import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.challege.repository.ChallengeRepository;
import com.example.santa.domain.report.entity.Report;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.S3ImageService;
import com.example.santa.global.util.mapsturct.ChallengeResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceImplTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserChallengeRepository userChallengeRepository;

    @Mock
    private ChallengeResponseMapper challengeResponseMapper;

    @Mock
    private S3ImageService s3ImageService;

    @InjectMocks
    private ChallengeServiceImpl challengeService;

    private ChallengeCreateDto challengeCreateDto;
    private Challenge challenge;
    private Category category;
    private ChallengeResponseDto challengeResponseDto;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        // Setup data
        category = new Category();
        category.setId(1L);
        category.setName("등산");

        challenge = new Challenge();
        challenge.setId(1L);
        challenge.setName("100대 명산 등반");
        challenge.setDescription("100대 명산을 전부 등반해보세요");
        challenge.setClearStandard(100);
        challenge.setImage("some-url");
        challenge.setCategory(category);

        challengeCreateDto = new ChallengeCreateDto();
        challengeCreateDto.setCategoryId(1L);
        challengeCreateDto.setName("100대 명산 등반");
        challengeCreateDto.setDescription("100대 명산을 전부 등반해보세요.");
        challengeCreateDto.setClearStandard(100);
        challengeCreateDto.setImage("some-url");
        challengeCreateDto.setImageFile(null);


        challengeResponseDto = new ChallengeResponseDto();
        challengeResponseDto.setId(challenge.getId());
        challengeResponseDto.setDescription(challenge.getDescription());
        challengeResponseDto.setClearStandard(challenge.getClearStandard());
        challengeResponseDto.setImage(challenge.getImage());
        challengeResponseDto.setName(challenge.getName());
//        challengeResponseDto.setCategory(challenge.getCategory());
        challengeResponseDto.setCategoryName(challenge.getCategory().name);
        lenient().when(challengeResponseMapper.toDto(any(Challenge.class))).thenReturn(challengeResponseDto);

    }

    @Test
    public void testSaveChallenge_WithDefaultImage() {
        // Setup the specific test scenario
        challengeCreateDto.setImageFile(null);

        // Mock the behavior of categoryRepository.findById() to return a valid Category object
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        when(challengeRepository.save(any(Challenge.class))).thenReturn(challenge);
        // Mock the behavior of challengeResponseMapper.toDto() to return a valid ChallengeResponseDto object
        when(challengeResponseMapper.toDto(challenge)).thenReturn(challengeResponseDto);

        // Call the method under test
        ChallengeResponseDto result = challengeService.saveChallenge(challengeCreateDto);

//    // Assert that the result is not null
        assertNotNull(result);

        // Assert that the result matches the expected challengeResponseDto
        assertEquals(challengeResponseDto, result);

        // Verify that the upload method of s3ImageService is never called
        verify(s3ImageService, never()).upload(any());
    }

    @Test
    public void testSaveChallenge_WithNewImage() {
        MultipartFile imageFile = new MockMultipartFile("file", "test.png", "image/png", "test image content".getBytes());
        challengeCreateDto.setImageFile(imageFile);
        //추가
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(s3ImageService.upload(any(MultipartFile.class))).thenReturn("uploaded-image-url");
        when(challengeRepository.save(any(Challenge.class))).thenReturn(challenge);
        when(challengeResponseMapper.toDto(challenge)).thenReturn(challengeResponseDto);

        ChallengeResponseDto result = challengeService.saveChallenge(challengeCreateDto);

        System.out.println(challenge.getName());
        System.out.println(result);
        assertNotNull(result);
        assertEquals(challengeResponseDto, result);
        verify(s3ImageService).upload(any(MultipartFile.class));
    }


    @Test
    public void testFindChallengeById_NotFound() {
        when(challengeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ChallengeResponseDto result = challengeService.findChallengeById(1L);

        assertNull(result);
    }

    @Test
    public void testFindChallengeById_Found() {

        when(challengeRepository.findById(1L)).thenReturn(Optional.of(challenge));

        ChallengeResponseDto result = challengeService.findChallengeById(1L);

        System.out.println(result);
        System.out.println(challengeResponseDto);
        assertNotNull(result);
        assertEquals(challengeResponseDto, result);
    }

    @Test
    public void testDeleteChallenge() {
        // This method might not necessarily need a return value check if it's void
        challengeService.deleteChallenge(1L);
        verify(challengeRepository).deleteById(1L);
    }

    @Test
    public void testSaveChallenge_CategoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ServiceLogicException.class, () -> {
            challengeService.saveChallenge(challengeCreateDto);
        });

        assertEquals("존재하지 않는 카테고리입니다.", exception.getMessage());
    }
}