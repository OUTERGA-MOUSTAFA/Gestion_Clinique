package com.clinique.gestion_clinique.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    private Long id;
}
