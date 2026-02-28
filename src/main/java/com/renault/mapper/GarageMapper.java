package com.renault.mapper;

import com.renault.dto.request.GarageRequestDto;
import com.renault.dto.request.OpeningHourDto;
import com.renault.dto.request.OpeningTimeDto;
import com.renault.dto.response.GarageResponseDto;
import com.renault.model.Garage;
import com.renault.model.OpeningHour;
import com.renault.model.OpeningTime;
import org.mapstruct.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface GarageMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    Garage toEntity(GarageRequestDto requestDTO);

    GarageResponseDto toDto(Garage garage);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    void updateGarageFromDto(GarageRequestDto dto, @MappingTarget Garage garage);


    default Map<DayOfWeek, OpeningHourDto> mapOpeningHoursToDto(
            Map<DayOfWeek, OpeningHour> openingHours
    ) {
        if (openingHours == null) {
            return null;
        }

        return openingHours.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            OpeningHour openingHour = entry.getValue();
                            List<OpeningTimeDto> openingTimeDTOs = openingHour.getOpeningTimes().stream()
                                    .map(openingTime -> new OpeningTimeDto(
                                            openingTime.getStartTime(),
                                            openingTime.getEndTime()
                                    ))
                                    .collect(Collectors.toList());
                            OpeningHourDto openingHourDto = new OpeningHourDto();
                            openingHourDto.setOpeningTimes(openingTimeDTOs);
                            return openingHourDto;
                        }
                ));
    }

    default Map<DayOfWeek, OpeningHour> mapOpeningHoursToEntity(
            Map<DayOfWeek, OpeningHourDto> openingHoursDto
    ) {
        if (openingHoursDto == null) {
            return null;
        }

        return openingHoursDto.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            OpeningHourDto openingHourDto = entry.getValue();
                            OpeningHour openingHour = new OpeningHour();
                            List<OpeningTime> openingTimes = openingHourDto.getOpeningTimes().stream()
                                    .map(openingTimeDto -> {
                                        OpeningTime openingTime = new OpeningTime();
                                        openingTime.setStartTime(openingTimeDto.startTime());
                                        openingTime.setEndTime(openingTimeDto.endTime());
                                        openingTime.setOpeningHour(openingHour);
                                        return openingTime;
                                    })
                                    .collect(Collectors.toList());
                            openingHour.setOpeningTimes(openingTimes);
                            return openingHour;
                        }
                ));
    }
}
