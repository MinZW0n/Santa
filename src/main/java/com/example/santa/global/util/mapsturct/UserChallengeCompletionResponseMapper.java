package com.example.santa.global.util.mapsturct;


import com.example.santa.domain.userchallenge.dto.UserChallengeCompletionResponseDto;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserChallengeCompletionResponseMapper extends EntityMapper<UserChallengeCompletionResponseDto, UserChallenge> {

    @Mapping(source ="challenge.name", target = "challengeName")
    @Mapping(source = "challenge.description", target = "challengeDescription")
    @Mapping(source = "challenge.image", target = "challengeImage")
    @Mapping(source = "challenge.clearStandard", target = "challengeClearStandard")
    UserChallengeCompletionResponseDto toDto(UserChallenge userChallenge);

}
