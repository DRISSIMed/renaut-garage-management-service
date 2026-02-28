package com.renault.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import com.renault.enums.DayOfWeek;
import java.util.Map;

public record GarageRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String address,
        @NotBlank
        String telephone,
        @NotBlank
        String email,
        @NotEmpty
        Map<DayOfWeek, OpeningHourDto> openingHoursList
) {
}
