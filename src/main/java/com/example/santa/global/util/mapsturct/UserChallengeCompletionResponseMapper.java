package com.example.santa.global.util.mapsturct;


import com.example.santa.domain.userchallenge.dto.UserChallengeCompletionResponseDto;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserChallengeCompletionResponseMapper extends EntityMapper<UserChallengeCompletionResponseDto, UserChallenge> {
}
