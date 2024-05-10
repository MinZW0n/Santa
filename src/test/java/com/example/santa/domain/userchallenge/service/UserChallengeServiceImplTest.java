package com.example.santa.domain.userchallenge.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.challege.repository.ChallengeRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.repository.UserMountainRepository;
import com.example.santa.global.exception.ServiceLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UserChallengeServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMountainRepository userMountainRepository;
    @Mock
    private ChallengeRepository challengeRepository;
    @Mock
    private UserChallengeRepository userChallengeRepository;

    @InjectMocks
    private UserChallengeServiceImpl userChallengeService;

    private User user;
    private UserMountain userMountain;
    private Challenge challenge;
    private UserChallenge userChallenge;

    @BeforeEach
    public void setUp() {
        user = new User();
        userMountain = new UserMountain();
        Category category = new Category();
        category.setId(1L);
        category.setName("100대 명산 등산");
        userMountain.setCategory(category);
        challenge = new Challenge();
        challenge.setId(1L);
        challenge.setClearStandard(5);
        userChallenge = new UserChallenge();
        userChallenge.setProgress(0);
        userChallenge.setUser(user);
        userChallenge.setChallenge(challenge);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        lenient().when(userMountainRepository.findById(anyLong())).thenReturn(Optional.of(userMountain));
        lenient().when(challengeRepository.findByCategoryName(anyString())).thenReturn(Arrays.asList(challenge));
        lenient().when(userChallengeRepository.findByUserAndChallengeId(any(User.class), anyLong())).thenReturn(Optional.of(userChallenge));
    }

    @Test
    public void updateProgress_UserAndUserMountainFound_ChallengesUpdated() {
        userChallengeService.updateProgress("test@example.com", 1L);

        assertEquals(1, userChallenge.getProgress());
        verify(userChallengeRepository, times(1)).save(userChallenge);
    }

    @Test
    public void updateProgress_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ServiceLogicException.class, () -> {
            userChallengeService.updateProgress("nonexistent@example.com", 1L);
        });

        assertEquals("존재하지 않는 회원입니다.", exception.getMessage());

    }

    @Test
    public void updateProgress_UserMountainNotFound_ThrowsException() {
        when(userMountainRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ServiceLogicException.class, () -> userChallengeService.updateProgress("test@example.com", 1L));
        assertEquals("존재하지 않는 유저 등산 정보입니다.", exception.getMessage());
        verify(userChallengeRepository, never()).save(any(UserChallenge.class));
    }


    @Test
    public void updateProgress_ChallengeCreationAndCompletion_Checks() {
        ArgumentCaptor<UserChallenge> userChallengeCaptor = ArgumentCaptor.forClass(UserChallenge.class);
        when(userChallengeRepository.findByUserAndChallengeId(user, challenge.getId())).thenReturn(Optional.empty());
        when(userChallengeRepository.save(any(UserChallenge.class))).thenAnswer(invocation -> invocation.getArgument(0));
        userChallengeService.updateProgress("test@example.com", 1L);
        verify(userChallengeRepository, times(2)).save(userChallengeCaptor.capture());

        List<UserChallenge> capturedChallenges = userChallengeCaptor.getAllValues();

        UserChallenge firstCapture = capturedChallenges.get(0);
        assertEquals(1, firstCapture.getProgress());
        assertNull(firstCapture.getIsCompleted());
        UserChallenge secondCapture = capturedChallenges.get(1);

    }
}