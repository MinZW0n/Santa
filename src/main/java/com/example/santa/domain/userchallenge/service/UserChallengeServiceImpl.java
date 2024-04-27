package com.example.santa.domain.userchallenge.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.challege.repository.ChallengeRepository;
import com.example.santa.domain.meeting.entity.Meeting;
import com.example.santa.domain.meeting.repository.MeetingRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.repository.UserMountainRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserChallengeServiceImpl implements UserChallengeService{
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final UserMountainRepository userMountainRepository;

    private final MeetingRepository meetingRepository;
    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    @Autowired
    public UserChallengeServiceImpl(UserChallengeRepository userChallengeRepository
            ,ChallengeRepository challengeRepository
            ,CategoryRepository categoryRepository
            ,UserRepository userRepository
            ,UserMountainRepository userMountainRepository
            ,MeetingRepository meetingRepository){
        this.userChallengeRepository = userChallengeRepository;
        this.challengeRepository =challengeRepository;
        this.categoryRepository = categoryRepository;
        this.meetingRepository = meetingRepository;
        this.userMountainRepository = userMountainRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void updateProgress(String email, Long userMountainId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        UserMountain userMountain = userMountainRepository.findById(userMountainId)
                .orElseThrow(() -> new IllegalArgumentException("유저 마운틴을 찾을 수 없습니다."));
        Category userMountainCategory = userMountain.getCategory();

        List<Challenge> challenges = challengeRepository.findByCategoryName(userMountainCategory.getName());


        for (Challenge challenge : challenges) {
            UserChallenge userChallenge = userChallengeRepository.findByUserAndChallengeId(user, challenge.getId())
                    .orElseGet(() -> {
                        // 새로운 UserChallenge 생성
                        UserChallenge newUserChallenge = UserChallenge.builder()
                                .user(user)
                                .challenge(challenge)
                                .progress(0) // 초기 진행 상태는 0
                                .build();
                        return userChallengeRepository.save(newUserChallenge);
                    });
            log.info("userChallenge {}", userChallenge);

            // progress 증가
            userChallenge.setProgress(userChallenge.getProgress() + 1);

            // clearStandard와 progress가 일치하면 성공 처리
            if (userChallenge.getProgress().equals(challenge.getClearStandard())) {
                userChallenge.setIsCompleted(true);
                userChallenge.setCompletionDate(LocalDate.now()); // 성공일자는 현재 날짜로 설정
            }

            userChallengeRepository.save(userChallenge);
        }

    }

    @Transactional
    public void updateUserChallengeOnMeetingJoin(Long meetingId,String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.MEETING_NOT_FOUND));
        Category meetingCategory = meeting.getCategory();

        // 사용자와 같은 카테고리의 챌린지 찾기
        List<Challenge> challenges = challengeRepository.findByCategoryName(meetingCategory.getName());

        for (Challenge challenge : challenges) {
            UserChallenge userChallenge = userChallengeRepository.findByUserAndChallengeId(user, challenge.getId())
                    .orElseGet(() -> {
                        // 새로운 UserChallenge 생성
                        UserChallenge newUserChallenge = UserChallenge.builder()
                                .user(user)
                                .challenge(challenge)
                                .progress(0) // 초기 진행 상태는 0
                                .build();
                        return userChallengeRepository.save(newUserChallenge);
                    });

            // progress 증가
            userChallenge.setProgress(userChallenge.getProgress() + 1);

            // clearStandard와 progress가 일치하면 성공 처리
            if (userChallenge.getProgress().equals(challenge.getClearStandard())) {
                userChallenge.setIsCompleted(true);
                userChallenge.setCompletionDate(LocalDate.now()); // 성공일자는 현재 날짜로 설정
            }

            userChallengeRepository.save(userChallenge);
        }
    }

}
