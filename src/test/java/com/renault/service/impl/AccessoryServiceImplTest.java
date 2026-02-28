package com.renault.service.impl;

import com.renault.dto.request.AccessoryRequestDto;
import com.renault.dto.response.AccessoryResponseDto;
import com.renault.exception.ResourceNotFoundException;
import com.renault.mapper.AccessoryMapper;
import com.renault.model.Accessory;
import com.renault.model.Vehicle;
import com.renault.repository.AccessoryRepository;
import com.renault.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccessoryServiceImplTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccessoryMapper accessoryMapper;

    @InjectMocks
    private AccessoryServiceImpl accessoryService;

    private Vehicle vehicle;
    private Accessory accessory;
    private AccessoryRequestDto requestDto;
    private AccessoryResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicle = Vehicle.builder()
                .id(1L)
                .brand("Renault")
                .manufacturingYear(2022)
                .fuelType("Diesel")
                .build();

        accessory = Accessory.builder()
                .id(1L)
                .name("Spoiler")
                .description("Sport spoiler")
                .price(200.0)
                .type("Exterior")
                .vehicle(vehicle)
                .build();

        requestDto = new AccessoryRequestDto(
                "Spoiler",
                "Sport spoiler",
                200,
                "Exterior"
        );

        responseDto = new AccessoryResponseDto(
                1L,
                "Spoiler",
                "Sport spoiler",
                200.0,
                "Exterior"
        );

    }

    @Test
    void createAccessory_shouldReturnResponseDto() throws ResourceNotFoundException {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toEntity(requestDto)).thenReturn(accessory);
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(accessory);
        when(accessoryMapper.toDto(accessory)).thenReturn(responseDto);

        AccessoryResponseDto result = accessoryService.createAccessory(1L, requestDto);

        assertNotNull(result);
        assertEquals("Spoiler", result.name());
        verify(accessoryRepository, times(1)).save(any(Accessory.class));
    }

    @Test
    void createAccessory_shouldThrowExceptionWhenVehicleNotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accessoryService.createAccessory(1L, requestDto));
    }

    @Test
    void updateAccessory_shouldReturnUpdatedDto() {
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));
        doNothing().when(accessoryMapper).updateAccessory(requestDto, accessory);
        when(accessoryRepository.save(accessory)).thenReturn(accessory);
        when(accessoryMapper.toDto(accessory)).thenReturn(responseDto);

        AccessoryResponseDto result = accessoryService.updateAccessory(1L, requestDto);

        assertNotNull(result);
        assertEquals("Spoiler", result.name());
    }

    @Test
    void updateAccessory_shouldThrowExceptionWhenNotFound() {
        when(accessoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> accessoryService.updateAccessory(1L, requestDto));
    }

    @Test
    void getAccessoriesByVehicleId_shouldReturnList() {
        when(accessoryRepository.findByVehicleId(1L)).thenReturn(List.of(accessory));
        when(accessoryMapper.toDto(accessory)).thenReturn(responseDto);

        List<AccessoryResponseDto> result = accessoryService.getAccessoriesByVehicleId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void deleteAccessory_shouldDeleteSuccessfully() throws ResourceNotFoundException {
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));

        accessoryService.deleteAccessory(1L);

        verify(accessoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAccessory_shouldThrowExceptionWhenNotFound() {
        when(accessoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accessoryService.deleteAccessory(1L));
    }
}