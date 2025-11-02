package org.example.patient.repository;


import org.example.patient.entity.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EncounterRepo extends JpaRepository<Encounter, Long> {
    List<Encounter> findByPatientId(Long patientId);
}
