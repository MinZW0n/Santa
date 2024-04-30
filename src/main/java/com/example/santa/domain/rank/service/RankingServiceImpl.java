package com.example.santa.domain.rank.service;

import com.example.santa.domain.rank.dto.RankingResponseDto;
import com.example.santa.domain.rank.entity.Ranking;
import com.example.santa.domain.rank.repository.RankingRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RankingServiceImpl implements RankingService{

    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;
    private final RankingRepository rankingRepository;

    public RankingServiceImpl(UserChallengeRepository userChallengeRepository, UserRepository userRepository, RankingRepository rankingRepository) {
        this.userChallengeRepository = userChallengeRepository;
        this.userRepository = userRepository;
        this.rankingRepository = rankingRepository;
    }

    @Transactional
//    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Scheduled(cron = "0 * * * * *") // 매분 0초마다 실행
    @Override
    public void updateAllRanks() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            long completedChallenges = userChallengeRepository.countCompletedChallengesByUserEmail(user.getEmail());
            double score = completedChallenges * 100 + user.getAccumulatedHeight();

            Ranking ranking = rankingRepository.findByUserEmail(user.getEmail()).orElse(new Ranking());
            ranking.setScore((int) score);
            ranking.setUser(user);
            rankingRepository.save(ranking);
        }
    }

    public Page<RankingResponseDto> getRankingOrderedByScore(Pageable pageable) {
        Page<Ranking> rankingsPage = rankingRepository.findAllByOrderByScoreDesc(pageable);

        return rankingsPage.map(ranking -> new RankingResponseDto(
                ranking.getId(),
                (long) (rankingRepository.countByScoreGreaterThan(ranking.getScore()) + 1),
                ranking.getUser().getNickname(),
                ranking.getUser().getImage(),
                ranking.getScore()
        ));
    }

    @Override
    public Optional<RankingResponseDto> getRankingByEmail(String email) {
        Optional<Ranking> ranking = rankingRepository.findByUserEmail(email);
        //Required type: Long
        //Provided: int
        return ranking.map(r -> new RankingResponseDto(
                r.getId(),
                (long) (rankingRepository.countByScoreGreaterThan(r.getScore()) + 1),
                r.getUser().getNickname(),
                r.getUser().getImage(),
                r.getScore()
        ));
    }

}
