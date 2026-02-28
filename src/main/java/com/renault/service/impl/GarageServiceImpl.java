package com.renault.service.impl;

import com.renault.dto.request.GarageRequestDto;
import com.renault.dto.response.GarageResponseDto;
import com.renault.mapper.GarageMapper;
import com.renault.model.Garage;
import com.renault.repository.GarageRepository;
import com.renault.service.GarageService;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GarageServiceImpl implements GarageService {
    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    public GarageServiceImpl(GarageRepository garageRepository, GarageMapper garageMapper) {
        this.garageRepository = garageRepository;
        this.garageMapper = garageMapper;
    }

    @Override
    public GarageResponseDto createGarage(GarageRequestDto dto) {
        Garage garage = garageMapper.toEntity(dto);
        return garageMapper.toDto(garageRepository.save(garage));
    }

@Override
public GarageResponseDto updateGarage(Long id, GarageRequestDto dto) {
    Optional<Garage> garage = Optional.of(garageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id)));
    garageMapper.updateGarageFromDto(dto, garage.get());
    return garageMapper.toDto(garageRepository.save(garage.get()));
}

@Override
public GarageResponseDto getGarageById(Long id) {
    Optional<Garage> garage = Optional.of(garageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id)));

    return garageMapper.toDto(garage.get());
}

@Override
public Page<GarageResponseDto> getAllGarages(Pageable pageable) {
    return garageRepository.findAll(pageable)
            .map(garageMapper::toDto);
}

@Override
public List<GarageResponseDto> findGaragesByAccessory(String accessoryName) {
    List<Garage> garages = garageRepository.findByVehiclesAccessoriesName(accessoryName);
    return garages
            .stream()
            .map(garageMapper::toDto)
            .toList();
}

@Override
public void deleteGarage(Long id) {
    if (!garageRepository.existsById(id)) {
        throw new ResourceNotFoundException("Garage not found with id: " + id);
    }
    garageRepository.deleteById(id);
}
}
