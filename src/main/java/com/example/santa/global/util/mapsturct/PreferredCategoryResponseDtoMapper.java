package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.preferredcategory.dto.PreferredCategoryResponseDto;
import com.example.santa.domain.preferredcategory.entity.PreferredCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PreferredCategoryResponseDtoMapper extends EntityMapper<PreferredCategoryResponseDto, PreferredCategory> {
}
