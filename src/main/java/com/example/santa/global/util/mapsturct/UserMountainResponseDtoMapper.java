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

//    UserMountainResponseDtoMapper INSTANCE = Mappers.getMapper(UserMountainResponseDtoMapper.class);

//    @Override
//    @Mapping(source = "user_id", target = "userId")
//    @Mapping(source = "mountain_id", target = "mountainId")
//    UserMountainResponseDto toDto(UserMountain entity);
//
//    @Override
//    default List<UserMountainResponseDto> toDtoList(List<UserMountain> entities) {
//        if (entities == null) {
//            return null;
//        }
//        return entities.stream().map(this::toDto).collect(Collectors.toList());
//    }

}
