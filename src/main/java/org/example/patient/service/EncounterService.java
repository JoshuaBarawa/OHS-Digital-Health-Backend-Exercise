package org.example.patient.service;

import org.example.patient.entity.Encounter;
import org.example.patient.entity.Patient;
import org.example.patient.models.EncounterDTO;
import org.example.patient.repository.EncounterRepo;
import org.example.patient.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EncounterService {

    @Autowired
    private EncounterRepo encounterRepository;

    @Autowired
    private PatientRepo patientRepository;


    public Optional<EncounterDTO> createEncounter(EncounterDTO encounterDTO) {
        Optional<Patient> patientOpt = patientRepository.findById(encounterDTO.getPatientId());

        if (patientOpt.isEmpty()) {
            return Optional.empty();
        }

        Encounter encounter = convertToEntity(encounterDTO, patientOpt.get());
        Encounter savedEncounter = encounterRepository.save(encounter);
        return Optional.of(convertToDTO(savedEncounter));
    }


    public Optional<EncounterDTO> getEncounterById(Long id) {
        return encounterRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<EncounterDTO> getAllEncounters() {
        return encounterRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EncounterDTO> getEncountersByPatientId(Long patientId) {
        return encounterRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public Optional<EncounterDTO> updateEncounter(Long id, EncounterDTO encounterDTO) {
        return encounterRepository.findById(id)
                .flatMap(encounter -> {
                    encounter.setStartDate(encounterDTO.getStartDate());
                    encounter.setEndDate(encounterDTO.getEndDate());
                    encounter.setEncounterClass(
                            Encounter.EncounterClass.valueOf(encounterDTO.getEncounterClass())
                    );

                    if (!encounter.getPatient().getId().equals(encounterDTO.getPatientId())) {
                        Optional<Patient> newPatient = patientRepository.findById(encounterDTO.getPatientId());
                        if (newPatient.isEmpty()) {
                            return Optional.empty();
                        }
                        encounter.setPatient(newPatient.get());
                    }

                    Encounter updatedEncounter = encounterRepository.save(encounter);
                    return Optional.of(convertToDTO(updatedEncounter));
                });
    }

    public boolean deleteEncounter(Long id) {
        if (encounterRepository.existsById(id)) {
            encounterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private EncounterDTO convertToDTO(Encounter encounter) {
        return new EncounterDTO(
                encounter.getPatient().getId(),
                encounter.getStartDate(),
                encounter.getEndDate(),
                encounter.getEncounterClass().name()
        );
    }

    private Encounter convertToEntity(EncounterDTO dto, Patient patient) {
        Encounter encounter = new Encounter();
        encounter.setPatient(patient);
        encounter.setStartDate(dto.getStartDate());
        encounter.setEndDate(dto.getEndDate());
        encounter.setEncounterClass(Encounter.EncounterClass.valueOf(dto.getEncounterClass()));
        return encounter;
    }
}
