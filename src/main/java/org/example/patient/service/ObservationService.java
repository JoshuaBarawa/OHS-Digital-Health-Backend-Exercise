package org.example.patient.service;

import org.example.patient.entity.Encounter;
import org.example.patient.entity.Observation;
import org.example.patient.entity.Patient;
import org.example.patient.models.ObservationDTO;
import org.example.patient.repository.EncounterRepo;
import org.example.patient.repository.ObservationRepo;
import org.example.patient.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ObservationService {

    @Autowired
    private ObservationRepo observationRepository;

    @Autowired
    private PatientRepo patientRepository;

    @Autowired
    private EncounterRepo encounterRepository;


    public Optional<ObservationDTO> createObservation(ObservationDTO observationDTO) {
        Optional<Patient> patientOpt = patientRepository.findById(observationDTO.getPatientId());

        if (patientOpt.isEmpty()) {
            return Optional.empty();
        }

        Observation observation = new Observation();
        observation.setPatient(patientOpt.get());


        if (observationDTO.getEncounterId() != null) {
            Optional<Encounter> encounterOpt = encounterRepository.findById(observationDTO.getEncounterId());
            encounterOpt.ifPresent(observation::setEncounter);
        }

        observation.setCode(observationDTO.getCode());
        observation.setValue(observationDTO.getValue());
        observation.setEffectiveDateTime(observationDTO.getEffectiveDateTime());

        Observation savedObservation = observationRepository.save(observation);
        return Optional.of(convertToDTO(savedObservation));
    }


    public Optional<ObservationDTO> getObservationById(Long id) {
        return observationRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<ObservationDTO> getAllObservations() {
        return observationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ObservationDTO> getObservationsByPatientId(Long patientId) {
        return observationRepository.findByPatientId(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ObservationDTO> getObservationsByEncounterId(Long encounterId) {
        return observationRepository.findByEncounterId(encounterId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public Optional<ObservationDTO> updateObservation(Long id, ObservationDTO observationDTO) {
        return observationRepository.findById(id)
                .flatMap(observation -> {
                    observation.setCode(observationDTO.getCode());
                    observation.setValue(observationDTO.getValue());
                    observation.setEffectiveDateTime(observationDTO.getEffectiveDateTime());


                    if (!observation.getPatient().getId().equals(observationDTO.getPatientId())) {
                        Optional<Patient> newPatient = patientRepository.findById(observationDTO.getPatientId());
                        if (newPatient.isEmpty()) {
                            return Optional.empty();
                        }
                        observation.setPatient(newPatient.get());
                    }


                    if (observationDTO.getEncounterId() != null) {
                        Optional<Encounter> encounterOpt = encounterRepository.findById(observationDTO.getEncounterId());
                        encounterOpt.ifPresent(observation::setEncounter);
                    } else {
                        observation.setEncounter(null);
                    }

                    Observation updatedObservation = observationRepository.save(observation);
                    return Optional.of(convertToDTO(updatedObservation));
                });
    }


    public boolean deleteObservation(Long id) {
        if (observationRepository.existsById(id)) {
            observationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ObservationDTO convertToDTO(Observation observation) {
        return new ObservationDTO(
                observation.getPatient().getId(),
                observation.getEncounter() != null ? observation.getEncounter().getId() : null,
                observation.getCode(),
                observation.getValue(),
                observation.getEffectiveDateTime()
        );
    }
}


