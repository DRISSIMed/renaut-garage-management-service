package com.renault.controller;

import com.renault.dto.request.AccessoryRequestDto;
import com.renault.dto.response.AccessoryResponseDto;
import com.renault.service.AccessoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accessories/v1")
public class AccessoryController {
    private final AccessoryService accessoryService;

    public AccessoryController(AccessoryService accessoryService) {
        this.accessoryService = accessoryService;
    }

    @PostMapping("/{vehicleId}")
    public ResponseEntity<AccessoryResponseDto> addAccessoryToVehicle(
            @PathVariable Long vehicleId,
            @RequestBody @Valid AccessoryRequestDto dto) {
        return ResponseEntity.ok(accessoryService.createAccessory(vehicleId, dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<AccessoryResponseDto> updateAccessory(
            @PathVariable Long id,
            @RequestBody @Valid AccessoryRequestDto dto) {
        return ResponseEntity.ok(accessoryService.updateAccessory(id, dto));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<List<AccessoryResponseDto>> getAccessoriesByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(accessoryService.getAccessoriesByVehicleId(vehicleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }



}
