package com.renault.dto.response;

public record VehicleResponseDto(
         Long id,
         String brand,
         Integer manufacturingYear,
         String fuelType
) {
}
