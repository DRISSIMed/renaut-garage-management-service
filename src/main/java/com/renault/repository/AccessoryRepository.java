package com.renault.repository;

import com.renault.model.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessoryRepository  extends JpaRepository<Accessory, Long> {
    List<Accessory> findByVehicleId(Long vehicleId);
}
