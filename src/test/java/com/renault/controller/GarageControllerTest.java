package com.renault.controller;

import com.renault.dto.request.OpeningHourDto;
import com.renault.dto.request.OpeningTimeDto;
import com.renault.dto.response.GarageResponseDto;
import com.renault.enums.DayOfWeek;

import com.renault.dto.request.GarageRequestDto;
import com.renault.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GarageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GarageRepository garageRepository;

    private GarageRequestDto requestDto;

    private GarageRequestDto requestDtoForUpdate;


    @BeforeEach
    void setUp() {
        garageRepository.deleteAll();

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

        requestDtoForUpdate = new GarageRequestDto(
                "Garage Maarif Updated",
                "Casablanca 22",
                "0609388743",
                "garageMarrif@gmail.rn",
                hours

        );
    }

    @Test
    void createGarage() throws Exception {
        mockMvc.perform(post("/api/garages/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage Maarif"))
                .andExpect(jsonPath("$.address").value("Casablanca"));
    }

    @Test
    void updateGarage() throws Exception {

        String response = mockMvc.perform(post("/api/garages/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        GarageResponseDto created = objectMapper.readValue(response, GarageResponseDto.class);


        mockMvc.perform(put("/api/garages/v1/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDtoForUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage Maarif Updated"));
    }

    @Test
    void getGarageById() {
    }

    @Test
    void searchGaragesByAccessory() {
    }

    @Test
    void getAllGarages() {
    }

    @Test
    void deleteGarage() throws Exception {
        String response = mockMvc.perform(post("/api/garages/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        GarageResponseDto created = objectMapper.readValue(response, GarageResponseDto.class);

        mockMvc.perform(delete("/api/garages/v1/{id}", created.id()))
                .andExpect(status().isNoContent());

    }
}