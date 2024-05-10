package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.userchallenge.dto.UserChallengeResponseDto;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserChallengeResponseMapper extends EntityMapper<UserChallengeResponseDto, UserChallenge> {
}
