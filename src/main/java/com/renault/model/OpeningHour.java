package com.renault.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class OpeningHour {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "openingHour", cascade = CascadeType.ALL)
    private List<OpeningTime> openingTimes = new ArrayList<>();

}