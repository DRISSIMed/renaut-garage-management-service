package com.renault.service;

import com.renault.dto.request.AccessoryRequestDto;
import com.renault.dto.response.AccessoryResponseDto;

import java.util.List;

public interface AccessoryService {
    AccessoryResponseDto createAccessory(Long vehicleId, AccessoryRequestDto dto);
    AccessoryResponseDto updateAccessory(Long id, AccessoryRequestDto dto);
    List<AccessoryResponseDto> getAccessoriesByVehicleId(Long vehicleId);
    void deleteAccessory(Long id);
}
