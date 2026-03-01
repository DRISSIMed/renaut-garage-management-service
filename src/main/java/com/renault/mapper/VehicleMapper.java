package com.renault.mapper;

import com.renault.dto.request.VehicleRequestDto;
import com.renault.dto.response.VehicleResponseDto;
import com.renault.model.Vehicle;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AccessoryMapper.class})
public interface VehicleMapper {
    Vehicle toEntity(VehicleRequestDto dto);
    VehicleResponseDto toDto(Vehicle entity);
    @Mapping(target = "accessories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVehicleFromDTO(VehicleRequestDto dto, @MappingTarget Vehicle entity);


}
