package com.renault.repository;

import com.renault.model.Garage;
import com.renault.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository  extends JpaRepository<Vehicle, Long> {
    long countByGarageId(Long garageId);
    List<Vehicle> findByGarage(Garage garage);
    List<Vehicle> findByBrandContainingAndGarageIdIn(String brand, List<Long> garageIds);


}
