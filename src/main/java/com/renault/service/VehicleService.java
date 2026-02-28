package com.renault.service;

import com.renault.dto.request.VehicleRequestDto;
import com.renault.dto.response.VehicleResponseDto;
import com.renault.exception.MaxVehiculeExceedException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface VehicleService {
    VehicleResponseDto createVehicle(Long garageId, VehicleRequestDto dto) throws MaxVehiculeExceedException, ExecutionException, InterruptedException;

    VehicleResponseDto updateVehicle(Long id, VehicleRequestDto dto);

    List<VehicleResponseDto> getVehicleByGarage(Long id);

    void deleteVehicle(Long id);

    List<VehicleResponseDto> getVehiclesByBrandAndGarages(String brand, List<Long> garageIds);
}
