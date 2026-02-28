package com.renault.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccessoryRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotNull
        double price,
        @NotBlank
        String type
) {}