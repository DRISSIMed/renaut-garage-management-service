package com.renault.service;

import com.renault.dto.request.GarageRequestDto;
import com.renault.dto.response.GarageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GarageService {
    GarageResponseDto createGarage(GarageRequestDto dto);

    GarageResponseDto updateGarage(Long id, GarageRequestDto dto);

    GarageResponseDto getGarageById(Long id);

    Page<GarageResponseDto> getAllGarages(Pageable pageable);

    List<GarageResponseDto> findGaragesByAccessory(String accessoryName);

    void deleteGarage(Long id);


}
