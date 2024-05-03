package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.mountain.dto.MountainResponseDto;
import com.example.santa.domain.mountain.entity.Mountain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MountainResponseDtoMapper extends EntityMapper<MountainResponseDto, Mountain>{
}
