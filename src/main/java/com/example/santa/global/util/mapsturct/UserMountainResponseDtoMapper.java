package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMountainResponseDtoMapper extends EntityMapper<UserMountainResponseDto, UserMountain>{

    @Mapping(source = "mountain.name", target = "mountainName")
    @Mapping(source = "mountain.location", target = "mountainLocation")
    @Mapping(source = "mountain.height", target = "mountainHeight")
    UserMountainResponseDto toDto(UserMountain userMountain);

}
