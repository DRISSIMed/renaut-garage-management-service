package com.renault.repository;

import com.renault.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GarageRepository extends JpaRepository<Garage, Long>, JpaSpecificationExecutor<Garage> {
    List<Garage> findByVehiclesAccessoriesName(String accessoryName);
}
