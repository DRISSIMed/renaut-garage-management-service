package com.renault.dto.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OpeningHourDto {
    List<OpeningTimeDto> openingTimes;
}