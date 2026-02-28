package com.renault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.*;
@Entity
@Table(name = "garages")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "day_of_week")
    @JoinColumn(name = "garage_id")
    private Map<DayOfWeek, OpeningHour> openingHours = new HashMap<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();
}
