package org.example.patient.service;

import org.example.patient.entity.Patient;
import org.example.patient.models.PatientDTO;
import org.example.patient.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepo patientRepository;

    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return convertToDTO(savedPatient);
    }


    public Optional<PatientDTO> getPatientById(Long id) {
        return patientRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PatientDTO> updatePatient(Long id, PatientDTO patientDTO) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setIdentifier(patientDTO.getIdentifier());
                    patient.setGivenName(patientDTO.getGivenName());
                    patient.setFamilyName(patientDTO.getFamilyName());
                    patient.setBirthDate(patientDTO.getBirthDate());
                    patient.setGender(Patient.Gender.valueOf(patientDTO.getGender()));
                    Patient updatedPatient = patientRepository.save(patient);
                    return convertToDTO(updatedPatient);
                });
    }


    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<PatientDTO> searchByFamilyName(String familyName) {
        return patientRepository.findByFamilyNameContainingIgnoreCase(familyName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<PatientDTO> searchByGivenName(String givenName) {
        return patientRepository.findByGivenNameContainingIgnoreCase(givenName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PatientDTO> searchByIdentifier(String identifier) {
        return patientRepository.findByIdentifier(identifier)
                .map(this::convertToDTO);
    }


    public List<PatientDTO> searchByBirthDate(LocalDate birthDate) {
        return patientRepository.findByBirthDate(birthDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<PatientDTO> searchByBirthDateRange(LocalDate startDate, LocalDate endDate) {
        return patientRepository.findByBirthDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
                patient.getIdentifier(),
                patient.getGivenName(),
                patient.getFamilyName(),
                patient.getBirthDate(),
                patient.getGender().name()
        );
    }

    private Patient convertToEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setIdentifier(dto.getIdentifier());
        patient.setGivenName(dto.getGivenName());
        patient.setFamilyName(dto.getFamilyName());
        patient.setBirthDate(dto.getBirthDate());
        patient.setGender(Patient.Gender.valueOf(dto.getGender()));
        return patient;
    }
}