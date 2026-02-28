package com.renault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "vehicles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String brand;

    @Column(name = "manufacturing_year")
    private Integer manufacturingYear;

    @Column(name = "fuel_type")
    private String fuelType;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Accessory> accessories = new ArrayList<>();
}
