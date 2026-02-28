package com.renault.mapper;

import com.renault.dto.request.OpeningTimeDto;
import com.renault.model.OpeningTime;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OpeningTimeMapper {
    OpeningTimeDto toDto(OpeningTime entity);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OpeningTime toEntity(OpeningTimeDto dto);

}
