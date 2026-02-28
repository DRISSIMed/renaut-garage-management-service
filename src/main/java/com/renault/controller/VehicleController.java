package com.renault.controller;

import com.renault.dto.request.VehicleRequestDto;
import com.renault.dto.response.VehicleResponseDto;
import com.renault.exception.MaxVehiculeExceedException;
import com.renault.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles/v1")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/{garageId}")
    public ResponseEntity<VehicleResponseDto> addVehicleToGarage(
            @PathVariable Long garageId,
            @RequestBody @Valid VehicleRequestDto vehicleRequestDto) throws MaxVehiculeExceedException {
        return ResponseEntity.ok(vehicleService.createVehicle(garageId, vehicleRequestDto));
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(
            @PathVariable Long vehicleId,
            @RequestBody @Valid VehicleRequestDto dto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicleId, dto));
    }
    @GetMapping("/garages/{garageId}")
    public ResponseEntity<List<VehicleResponseDto>> getVehiclesByGarage(
            @PathVariable Long garageId) {
        return ResponseEntity.ok(vehicleService.getVehicleByGarage(garageId));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<VehicleResponseDto>> getVehiclesByBrand(
            @PathVariable String brand,
            @RequestParam List<Long> garageIds) {
        return ResponseEntity.ok(vehicleService.getVehiclesByBrandAndGarages(brand, garageIds));
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }
}
