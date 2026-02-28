package com.renault.mapper;

import com.renault.dto.request.AccessoryRequestDto;

import com.renault.dto.response.AccessoryResponseDto;
import com.renault.model.Accessory;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;



@Mapper(componentModel = "spring")
public interface AccessoryMapper {
    AccessoryResponseDto toDto(Accessory entity);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Accessory toEntity(AccessoryRequestDto dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccessory(AccessoryRequestDto dto, @MappingTarget Accessory entity);


}
