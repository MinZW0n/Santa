package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.challege.entity.Challenge;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeResponseMapper extends EntityMapper<ChallengeResponseDto, Challenge> {
}
