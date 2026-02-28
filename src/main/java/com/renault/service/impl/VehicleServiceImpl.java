package com.renault.service.impl;

import com.renault.dto.request.VehicleRequestDto;
import com.renault.dto.response.VehicleResponseDto;
import com.renault.exception.MaxVehiculeExceedException;
import com.renault.kafka.VehicleEventPublisher;
import com.renault.mapper.VehicleMapper;
import com.renault.model.Garage;
import com.renault.model.Vehicle;
import com.renault.repository.GarageRepository;
import com.renault.repository.VehicleRepository;
import com.renault.service.VehicleService;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    @Value("${garage.vehicle.quota}")
    private int  GARAGE_VEHICLE_QUOTA;
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final GarageRepository garageRepository;
    private final VehicleEventPublisher vehicleEventPublisher;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper, GarageRepository garageRepository, VehicleEventPublisher vehicleEventPublisher) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.garageRepository = garageRepository;
        this.vehicleEventPublisher = vehicleEventPublisher;
    }

    @Override
    public VehicleResponseDto createVehicle(Long garageId, VehicleRequestDto dto) throws MaxVehiculeExceedException {
        Optional<Garage> garage = Optional.of(garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId)));

        long currentCount = vehicleRepository.countByGarageId(garageId);
        if (currentCount >= GARAGE_VEHICLE_QUOTA) {
            throw new MaxVehiculeExceedException("Garage " + garageId + " is out of quota limit " + GARAGE_VEHICLE_QUOTA + " vehicles.");
        }

        Vehicle vehicle = vehicleMapper.toEntity(dto);
        vehicle.setGarage(garage.get());

        VehicleResponseDto responseDto = vehicleMapper.toDto(vehicleRepository.save(vehicle));

        //vehicleEventPublisher.publishVehicleCreated(responseDto);

        return responseDto;
    }

    @Override
    public VehicleResponseDto updateVehicle(Long id, VehicleRequestDto dto) {
        Optional<Vehicle> vehicle = Optional.of(vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id)));
        vehicleMapper.updateVehicleFromDTO(dto, vehicle.get());
        return vehicleMapper.toDto(vehicleRepository.save(vehicle.get()));
    }

    @Override
    public List<VehicleResponseDto> getVehicleByGarage(Long id) {
        Optional<Garage>  garage = Optional.of(garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id)));
        return vehicleRepository.findByGarage(garage.get())
                .stream()
                .map(vehicleMapper::toDto)
                .toList();
    }

    @Override
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public List<VehicleResponseDto> getVehiclesByBrandAndGarages(String brand, List<Long> garageIds) {
        if (garageIds == null || garageIds.isEmpty()) {
             throw new RuntimeException("garage missing during search");
        }
        List<Vehicle> vehicles = vehicleRepository.findByBrandContainingAndGarageIdIn(brand, garageIds);

        return vehicles.stream()
                .map(vehicleMapper::toDto)
                .toList();
    }
}
