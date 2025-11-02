package org.example.patient.repository;

import org.example.patient.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservationRepo extends JpaRepository<Observation, Long> {
    List<Observation> findByPatientId(Long patientId);
    List<Observation> findByEncounterId(Long encounterId);
    List<Observation> findByCode(String code);
}
