package com.example.santa.domain.challege.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.challege.dto.ChallengeCreateDto;
import com.example.santa.domain.challege.dto.ChallengeParticipationResponseDto;
import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import com.example.santa.global.constant.Constants;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.S3ImageService;
import com.example.santa.global.util.mapsturct.ChallengeResponseMapper;
import com.example.santa.domain.challege.repository.ChallengeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ChallengeServiceImpl implements ChallengeService{

    private final ChallengeRepository challengeRepository;
    private final ChallengeResponseMapper challengeResponseMapper;


    private final CategoryRepository categoryRepository;

    private final UserChallengeRepository userChallengeRepository;

    private final S3ImageService s3ImageService;

    @Autowired
    public ChallengeServiceImpl(ChallengeRepository challengeRepository, CategoryRepository categoryRepository, UserChallengeRepository userChallengeRepository, ChallengeResponseMapper challengeResponseMapper,  S3ImageService s3ImageService) {
        this.challengeRepository = challengeRepository;
        this.categoryRepository = categoryRepository;
        this.userChallengeRepository =userChallengeRepository;
        this.challengeResponseMapper = challengeResponseMapper;
        this.s3ImageService = s3ImageService;
    }


    //CREATE
    @Transactional
    @Override
    public ChallengeResponseDto saveChallenge(ChallengeCreateDto challengeCreateDto) {
        Category category = categoryRepository.findById(challengeCreateDto.getCategoryId())
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.CATEGORY_NOT_FOUND));

        MultipartFile imageFile = challengeCreateDto.getImageFile();
        String imageUrl = Constants.DEFAULT_URL + "challenge_default_image.png";
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3ImageService.upload(imageFile);
        }


        Challenge save = challengeRepository.save(Challenge.builder()
                .category(category)
                .name(challengeCreateDto.getName())
                .description(challengeCreateDto.getDescription())
                .clearStandard(challengeCreateDto.getClearStandard())
                .image(imageUrl)
                .build());
        return challengeResponseMapper.toDto(save);
    }


    @Override
    public Page<ChallengeResponseDto> findAllChallenges(Pageable pageable){
        Page<Challenge> challenges =challengeRepository.findAll(pageable);
        return challenges.map(challengeResponseMapper::toDto);
    }


    @Override
    public ChallengeResponseDto findChallengeById(Long id) {
        return challengeRepository.findById(id)
                .map(challengeResponseMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChallengeParticipationResponseDto> getUsersParticipationInChallenges() {
//        List<ChallengeParticipationResponseDto> challengeParticipations = userChallengeRepository.countUsersPerChallenge()
//                .
//        ChallengeParticipationResponseDto challengeParticipations = userChallengeRepository.countUsersPerChallenge();
        return userChallengeRepository.countUsersPerChallenge();
    }


    @Override
    public ChallengeResponseDto updateChallenge(Long id, ChallengeCreateDto challengeCreateDto) {
        MultipartFile imageFile = challengeCreateDto.getImageFile();
        String imageUrl = challengeCreateDto.getImage();
        if(!Objects.equals(imageUrl, Constants.DEFAULT_URL + "challenge_default_image.png")){
            s3ImageService.deleteImageFromS3(imageUrl);
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3ImageService.upload(imageFile);
        }
        ChallengeResponseDto result = null;
        if (challengeRepository.existsById(id)) {
            Challenge challenge = challengeRepository.findById(id).get();
            challenge.setName(challengeCreateDto.getName());
            challenge.setDescription(challengeCreateDto.getDescription());
            challenge.setImage(imageUrl);
            challenge.setClearStandard(challengeCreateDto.getClearStandard());
            challenge = challengeRepository.save(challenge);
            result = challengeResponseMapper.toDto(challenge);
        }
        return result;
    }

    @Override
    public void deleteChallenge(Long id) {

        challengeRepository.deleteById(id);
    }

}

