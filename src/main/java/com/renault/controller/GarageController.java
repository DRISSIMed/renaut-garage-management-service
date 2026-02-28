package com.renault.controller;

import com.renault.dto.request.GarageRequestDto;
import com.renault.dto.response.GarageResponseDto;
import com.renault.service.GarageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garages/v1")
public class GarageController {
    private final GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<GarageResponseDto> createGarage(@RequestBody  GarageRequestDto dto) {
        return ResponseEntity.ok(garageService.createGarage(dto));
    }

    @PutMapping("/{garageId}")
    public ResponseEntity<GarageResponseDto> updateGarage(
            @PathVariable Long garageId,
            @RequestBody @Valid GarageRequestDto dto) {
        return ResponseEntity.ok(garageService.updateGarage(garageId, dto));
    }

    @GetMapping("/{garageId}")
    public ResponseEntity<GarageResponseDto> getGarageById(@PathVariable Long garageId) {
        return ResponseEntity.ok(garageService.getGarageById(garageId));
    }

    @GetMapping("/search-by-accessory/{accessoryName}")
    public ResponseEntity<List<GarageResponseDto>> searchGaragesByAccessory(@PathVariable String accessoryName) {
        return ResponseEntity.ok(garageService.findGaragesByAccessory(accessoryName));
    }

    @GetMapping
    public ResponseEntity<Page<GarageResponseDto>> getAllGarages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy
    ) {
        Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable p = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(garageService.getAllGarages(p));
    }

    @DeleteMapping("/{garageId}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long garageId) {
        garageService.deleteGarage(garageId);
        return ResponseEntity.noContent().build();
    }


}
