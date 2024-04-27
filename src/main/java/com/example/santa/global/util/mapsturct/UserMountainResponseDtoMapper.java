package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMountainResponseDtoMapper extends EntityMapper<UserMountainResponseDto, UserMountain>{

}
