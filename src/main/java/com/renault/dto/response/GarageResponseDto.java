package com.renault.dto.response;

import com.renault.dto.request.OpeningHourDto;

import com.renault.enums.DayOfWeek;
import java.util.Map;

public record GarageResponseDto(
         Long id,
         String name,
         String address,
         String telephone,
         String email,
         Map<DayOfWeek, OpeningHourDto> openingHoursList
) {
}
