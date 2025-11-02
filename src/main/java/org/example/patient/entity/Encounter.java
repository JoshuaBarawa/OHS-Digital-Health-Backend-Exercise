package org.example.patient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "encounters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EncounterClass encounterClass;

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Observation> observations;

    public enum EncounterClass {
        INPATIENT, OUTPATIENT, EMERGENCY
    }
}
