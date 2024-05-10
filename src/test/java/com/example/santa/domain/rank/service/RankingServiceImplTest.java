package com.example.santa.domain.rank.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.santa.domain.rank.dto.RankingResponseDto;
import com.example.santa.domain.rank.entity.Ranking;
import com.example.santa.domain.rank.repository.RankingRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RankingServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserChallengeRepository userChallengeRepository;

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private RankingServiceImpl rankingService;

    private User user;
    private Ranking ranking;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@email.com");
        user.setNickname("닉네임");

        ranking = new Ranking();
        ranking.setUser(user);
        ranking.setScore(1000);
        pageable = Pageable.unpaged();
    }


    @Test
    void updateAllRanks_shouldUpdateRankings() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userChallengeRepository.countCompletedChallengesByUserEmail(user.getEmail())).thenReturn(5L);

        rankingService.updateAllRanks();

        verify(userRepository).findAll();
        verify(userChallengeRepository).countCompletedChallengesByUserEmail(user.getEmail());
        verify(rankingRepository).save(any(Ranking.class));
    }

    @Test
    void getRankingOrderedByScore_shouldReturnRankings() {
        List<Ranking> rankings = Arrays.asList(ranking);
        Page<Ranking> rankingPage = new PageImpl<>(rankings);
        when(rankingRepository.findAllByOrderByScoreDesc(pageable)).thenReturn(rankingPage);

        Page<RankingResponseDto> result = rankingService.getRankingOrderedByScore(pageable);

        verify(rankingRepository).findAllByOrderByScoreDesc(pageable);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("닉네임", result.getContent().get(0).getNickname());
    }

    @Test
    void getRankingByEmail_shouldReturnRankingDto() {
        when(rankingRepository.findByUserEmail("user@example.com")).thenReturn(Optional.of(ranking));
        when(rankingRepository.countByScoreGreaterThan(ranking.getScore())).thenReturn(2);

        Optional<RankingResponseDto> result = rankingService.getRankingByEmail("user@example.com");

        assertTrue(result.isPresent());
        assertEquals("닉네임", result.get().getNickname());
        assertEquals(Integer.valueOf(1000), result.get().getScore());
    }
}