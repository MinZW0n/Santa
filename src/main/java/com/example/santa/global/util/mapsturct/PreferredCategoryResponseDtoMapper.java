package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.preferredcategory.dto.PreferredCategoryResponseDto;
import com.example.santa.domain.preferredcategory.entity.PreferredCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PreferredCategoryResponseDtoMapper extends EntityMapper<PreferredCategoryResponseDto, PreferredCategory> {

    @Mapping(source = "category.name", target = "categoryName")
    PreferredCategoryResponseDto toDto(PreferredCategory preferredCategory);

}
