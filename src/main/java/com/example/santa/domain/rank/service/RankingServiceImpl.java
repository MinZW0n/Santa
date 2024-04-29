package com.example.santa.domain.rank.service;

import com.example.santa.domain.rank.dto.RankingReponseDto;
import com.example.santa.domain.rank.entity.Ranking;
import com.example.santa.domain.rank.repository.RankingRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<RankingReponseDto> getRankingOrderedByScore() {
        List<Ranking> rankings = rankingRepository.findAllByOrderByScoreDesc();
        // Ranking 정보를 담을 객체를 저장할 리스트 생성.
        List<RankingReponseDto> rankingDtos = new ArrayList<>();
        // 순위 표시 변수
        long rank = 1;
        for (Ranking ranking : rankings) {
            // 현재 순위, 사용자 이름, 점수를 이용하여 새로운 객체 생성
            rankingDtos.add(new RankingReponseDto(rank++, ranking.getId(),ranking.getUser().getName(), ranking.getScore()));
        }
        return rankingDtos;
    }
}
