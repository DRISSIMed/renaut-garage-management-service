package com.renault.dto.response;

public record AccessoryResponseDto(
         Long id,
         String name,
         String description,
         Double price,
         String type
) {
}
