package com.renault.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleRequestDto(
        @NotBlank
        String brand,
        @NotNull
        int manufacturingYear,
        @NotBlank
        String fuelType
) {}