package com.example.santa.challege.mapper;

import com.example.santa.challege.dto.ChallengeCreateDto;
import com.example.santa.challege.dto.ChallengeResponseDto;
import com.example.santa.challege.entity.Challenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    ChallengeResponseDto entityToDto(Challenge challenge);
}
