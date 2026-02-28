package com.renault.service.impl;

import com.renault.dto.request.AccessoryRequestDto;
import com.renault.dto.response.AccessoryResponseDto;
import com.renault.exception.ResourceNotFoundException;
import com.renault.mapper.AccessoryMapper;
import com.renault.model.Accessory;
import com.renault.model.Vehicle;
import com.renault.repository.AccessoryRepository;
import com.renault.repository.VehicleRepository;
import com.renault.service.AccessoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessoryServiceImpl implements AccessoryService {
    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper accessoryMapper;

    @Override
    public AccessoryResponseDto createAccessory(Long vehicleId, AccessoryRequestDto dto) throws ResourceNotFoundException {
        Optional<Vehicle> vehicle = Optional.of(vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId)));
        Accessory accessory = accessoryMapper.toEntity(dto);
        accessory.setVehicle(vehicle.get());

        return accessoryMapper.toDto(accessoryRepository.save(accessory));
    }

    @Override
    public AccessoryResponseDto updateAccessory(Long id, AccessoryRequestDto dto) {
        Optional<Accessory> accessory = Optional.of(accessoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Accessory not found with id: " + id)));
        accessoryMapper.updateAccessory(dto, accessory.get());
        return accessoryMapper.toDto(accessoryRepository.save(accessory.get()));
    }

    @Override
    public List<AccessoryResponseDto> getAccessoriesByVehicleId(Long vehicleId) {
        return accessoryRepository.findByVehicleId(vehicleId)
                .stream()
                .map(accessoryMapper::toDto)
                .toList();
    }

    @Override
    public void deleteAccessory(Long id) throws ResourceNotFoundException {
        if(accessoryRepository.findById(id).isEmpty()){
            throw new ResourceNotFoundException("Accessory not found with id: " + id);
        }

        accessoryRepository.deleteById(id);
    }
}
