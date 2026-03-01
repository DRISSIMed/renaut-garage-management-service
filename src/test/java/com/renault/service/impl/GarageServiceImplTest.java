package com.renault.service.impl;

import com.renault.dto.request.OpeningHourDto;
import com.renault.dto.request.OpeningTimeDto;
import com.renault.enums.DayOfWeek;
import com.renault.model.OpeningHour;
import org.junit.jupiter.api.Test;

import com.renault.dto.request.GarageRequestDto;
import com.renault.dto.response.GarageResponseDto;
import com.renault.mapper.GarageMapper;
import com.renault.model.Garage;
import com.renault.repository.GarageRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GarageServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private GarageServiceImpl garageService;

    private Garage garage;
    private GarageRequestDto requestDto;
    private GarageResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        garage = Garage.builder()
                .id(1L)
                .name("Renault Garage")
                .address("123 Main Street")
                .telephone("0600000000")
                .email("garage@renault.com")
                .build();

        Map<DayOfWeek, OpeningHourDto> hours = new HashMap<>();
        List<OpeningTimeDto> openingTimes =new ArrayList<>();
        OpeningTimeDto openingTimeDto1 =new OpeningTimeDto(LocalTime.of(8,30,00),LocalTime.of(12,30,00));
        OpeningTimeDto openingTimeDto2 =new OpeningTimeDto(LocalTime.of(9,30,00),LocalTime.of(12,30,00));
        openingTimes.add(openingTimeDto1);
        openingTimes.add(openingTimeDto2);
        OpeningHourDto openingHourDto =new OpeningHourDto();
        openingHourDto.setOpeningTimes(openingTimes);
        hours.put(DayOfWeek.MONDAY, openingHourDto);

        requestDto = new GarageRequestDto(
                "Garage Maarif",
                "Casablanca",
                "0609388743",
                "garageMarrif@gmail.rn",
                hours

        );


        responseDto = new GarageResponseDto(
                1L,
                "Renault Garage",
                "123 Main Street",
                "0600000000",
                "garage@renault.com",
                null
        );
    }

    @Test
    void createGarage_shouldReturnResponseDto() {
        when(garageMapper.toEntity(requestDto)).thenReturn(garage);
        when(garageRepository.save(garage)).thenReturn(garage);
        when(garageMapper.toDto(garage)).thenReturn(responseDto);

        GarageResponseDto result = garageService.createGarage(requestDto);

        assertNotNull(result);
        assertEquals("Renault Garage", result.name());
        verify(garageRepository, times(1)).save(garage);
    }

    @Test
    void updateGarage_shouldReturnUpdatedDto() {
        Map<DayOfWeek, OpeningHourDto> openingHours = Map.of(
                DayOfWeek.MONDAY, new OpeningHourDto(List.of(new OpeningTimeDto(LocalTime.of(9, 0), LocalTime.of(18, 0))))
        );
        GarageRequestDto requestDto = new GarageRequestDto(
                "Renault Garage", "123 Street", "0600000000", "garage@renault.com", openingHours
        );
        Garage garage = new Garage();
        garage.setId(1L);
        garage.setOpeningHoursList(new HashMap<>());
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        doNothing().when(garageMapper).updateGarageFromDto(requestDto, garage);
        when(garageRepository.save(garage)).thenReturn(garage);
        when(garageMapper.toDto(garage)).thenReturn(responseDto);
        GarageResponseDto result = garageService.updateGarage(1L, requestDto);
        assertNotNull(result);
        assertEquals("Renault Garage", result.name());
        assertFalse(garage.getOpeningHoursList().isEmpty());
        assertTrue(garage.getOpeningHoursList().containsKey(DayOfWeek.MONDAY));

        verify(garageRepository).findById(1L);
        verify(garageMapper).updateGarageFromDto(requestDto, garage);
        verify(garageRepository).save(garage);
        verify(garageMapper).toDto(garage);
    }

    @Test
    void updateGarage_shouldThrowExceptionWhenNotFound() {
        when(garageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> garageService.updateGarage(1L, requestDto));
    }

    @Test
    void getGarageById_shouldReturnDto() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageMapper.toDto(garage)).thenReturn(responseDto);

        GarageResponseDto result = garageService.getGarageById(1L);

        assertNotNull(result);
        assertEquals("Renault Garage", result.name());
    }

    @Test
    void getGarageById_shouldThrowExceptionWhenNotFound() {
        when(garageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> garageService.getGarageById(1L));
    }

    @Test
    void getAllGarages_shouldReturnPage() {
        Page<Garage> page = new PageImpl<>(List.of(garage));
        when(garageRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(garageMapper.toDto(garage)).thenReturn(responseDto);

        Page<GarageResponseDto> result = garageService.getAllGarages(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findGaragesByAccessory_shouldReturnList() {
        when(garageRepository.findByVehiclesAccessoriesName("Spoiler")).thenReturn(List.of(garage));
        when(garageMapper.toDto(garage)).thenReturn(responseDto);

        List<GarageResponseDto> result = garageService.findGaragesByAccessory("Spoiler");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void deleteGarage_shouldDeleteSuccessfully() {
        when(garageRepository.existsById(1L)).thenReturn(true);

        garageService.deleteGarage(1L);

        verify(garageRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGarage_shouldThrowExceptionWhenNotFound() {
        when(garageRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> garageService.deleteGarage(1L));
    }
}