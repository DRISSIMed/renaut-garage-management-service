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
import com.renault.service.impl.VehicleServiceImpl;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleEventPublisher vehicleEventPublisher;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Garage garage;
    private Vehicle vehicle;
    private VehicleRequestDto requestDto;
    private VehicleResponseDto responseDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(vehicleService, "GARAGE_VEHICLE_QUOTA", 2);

        garage = Garage.builder()
                .id(1L)
                .name("Garage Test")
                .build();

        vehicle = Vehicle.builder()
                .id(1L)
                .brand("Renault")
                .manufacturingYear(2020)
                .fuelType("Diesel")
                .build();

        requestDto = new VehicleRequestDto("Renault", 2020, "Diesel");

        responseDto = new VehicleResponseDto(1L, "Renault", 2020, "Diesel");
    }

    @Test
    void shouldCreateVehicleSuccessfully() throws Exception {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(1L)).thenReturn(1L);
        when(vehicleMapper.toEntity(requestDto)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toDto(vehicle)).thenReturn(responseDto);

        VehicleResponseDto result = vehicleService.createVehicle(1L, requestDto);

        assertThat(result).isNotNull();
        assertThat(result.brand()).isEqualTo("Renault");

        verify(vehicleRepository).save(vehicle);
        verify(vehicleEventPublisher).publishVehicleCreated(responseDto);
    }

    @Test
    void shouldThrowExceptionWhenGarageNotFound() {
        when(garageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.createVehicle(1L, requestDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldThrowMaxVehiculeExceedExceptionWhenQuotaReached() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(1L)).thenReturn(2L);

        assertThatThrownBy(() -> vehicleService.createVehicle(1L, requestDto))
                .isInstanceOf(MaxVehiculeExceedException.class);
    }

    @Test
    void shouldUpdateVehicleSuccessfully() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toDto(vehicle)).thenReturn(responseDto);

        VehicleResponseDto result = vehicleService.updateVehicle(1L, requestDto);

        assertThat(result).isNotNull();
        verify(vehicleMapper).updateVehicleFromDTO(requestDto, vehicle);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void shouldThrowExceptionWhenVehicleToUpdateNotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.updateVehicle(1L, requestDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldReturnVehiclesByGarage() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.findByGarage(garage)).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDto(vehicle)).thenReturn(responseDto);

        List<VehicleResponseDto> result = vehicleService.getVehicleByGarage(1L);

        assertThat(result).hasSize(1);
        verify(vehicleRepository).findByGarage(garage);
    }

    @Test
    void shouldThrowExceptionWhenGarageNotFoundInGetByGarage() {
        when(garageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.getVehicleByGarage(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldDeleteVehicleSuccessfully() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);

        vehicleService.deleteVehicle(1L);

        verify(vehicleRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingVehicle() {
        when(vehicleRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.deleteVehicle(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldReturnVehiclesByBrandAndGarageIds() {
        when(vehicleRepository.findByBrandContainingAndGarageIdIn("Ren", List.of(1L)))
                .thenReturn(List.of(vehicle));
        when(vehicleMapper.toDto(vehicle)).thenReturn(responseDto);

        List<VehicleResponseDto> result =
                vehicleService.getVehiclesByBrandAndGarages("Ren", List.of(1L));

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldThrowExceptionWhenGarageIdsMissing() {
        assertThatThrownBy(() ->
                vehicleService.getVehiclesByBrandAndGarages("Ren", List.of()))
                .isInstanceOf(RuntimeException.class);
    }
}