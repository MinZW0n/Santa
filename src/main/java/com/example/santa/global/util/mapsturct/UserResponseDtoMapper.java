package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseDtoMapper extends EntityMapper<UserResponseDto, User> {
}
