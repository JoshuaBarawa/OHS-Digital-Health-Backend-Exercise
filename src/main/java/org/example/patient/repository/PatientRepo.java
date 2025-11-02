package org.example.patient.repository;

import org.example.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {
    Optional<Patient> findByIdentifier(String identifier);
    List<Patient> findByFamilyNameContainingIgnoreCase(String familyName);
    List<Patient> findByGivenNameContainingIgnoreCase(String givenName);
    List<Patient> findByBirthDate(LocalDate birthDate);
    List<Patient> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
}
