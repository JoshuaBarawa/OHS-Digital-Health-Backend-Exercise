package org.example.patient.service;

import org.example.patient.entity.Patient;
import org.example.patient.models.PatientDTO;
import org.example.patient.repository.PatientRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Patient Service Tests")
class PatientServiceTest {

    @Mock
    private PatientRepo patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setIdentifier("MRN-12345");
        patient.setGivenName("John");
        patient.setFamilyName("Doe");
        patient.setBirthDate(LocalDate.of(1985, 6, 15));
        patient.setGender(Patient.Gender.MALE);

        patientDTO = new PatientDTO(
                "MRN-12345",
                "John",
                "Doe",
                LocalDate.of(1985, 6, 15),
                "MALE"
        );
    }

    @Test
    @DisplayName("Test 1: Should create patient successfully")
    void testCreatePatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        PatientDTO result = patientService.createPatient(patientDTO);

        assertNotNull(result);
        assertEquals("MRN-12345", result.getIdentifier());
        assertEquals("John", result.getGivenName());
        assertEquals("Doe", result.getFamilyName());
        assertEquals(LocalDate.of(1985, 6, 15), result.getBirthDate());
        assertEquals("MALE", result.getGender());

        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Test 2: Should get patient by ID successfully")
    void testGetPatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<PatientDTO> result = patientService.getPatientById(1L);

        assertTrue(result.isPresent());
        assertEquals("MRN-12345", result.get().getIdentifier());
        assertEquals("John", result.get().getGivenName());

        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test 3: Should get all patients successfully")
    void testGetAllPatients() {
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setIdentifier("MRN-67890");
        patient2.setGivenName("Jane");
        patient2.setFamilyName("Smith");
        patient2.setBirthDate(LocalDate.of(1990, 3, 20));
        patient2.setGender(Patient.Gender.FEMALE);

        List<Patient> patients = Arrays.asList(patient, patient2);
        when(patientRepository.findAll()).thenReturn(patients);

        List<PatientDTO> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getGivenName());
        assertEquals("Jane", result.get(1).getGivenName());

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 4: Should update patient successfully")
    void testUpdatePatient() {

        PatientDTO updatedDTO = new PatientDTO(
                "MRN-12345",
                "John",
                "Smith",
                LocalDate.of(1985, 6, 15),
                "MALE"
        );

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setIdentifier("MRN-12345");
        updatedPatient.setGivenName("John");
        updatedPatient.setFamilyName("Smith");
        updatedPatient.setBirthDate(LocalDate.of(1985, 6, 15));
        updatedPatient.setGender(Patient.Gender.MALE);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        Optional<PatientDTO> result = patientService.updatePatient(1L, updatedDTO);

        assertTrue(result.isPresent());
        assertEquals("Smith", result.get().getFamilyName());

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Test 5: Should delete patient successfully")
    void testDeletePatient() {

        when(patientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(patientRepository).deleteById(1L);

        boolean result = patientService.deletePatient(1L);

        assertTrue(result);
        verify(patientRepository, times(1)).existsById(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test 6: Should search patients by family name")
    void testSearchByFamilyName() {

        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findByFamilyNameContainingIgnoreCase("Doe")).thenReturn(patients);

        List<PatientDTO> result = patientService.searchByFamilyName("Doe");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getFamilyName());

        verify(patientRepository, times(1)).findByFamilyNameContainingIgnoreCase("Doe");
    }

    @Test
    @DisplayName("Test 7: Should search patients by birth date range")
    void testSearchByBirthDateRange() {

        LocalDate startDate = LocalDate.of(1980, 1, 1);
        LocalDate endDate = LocalDate.of(1990, 12, 31);
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findByBirthDateBetween(startDate, endDate)).thenReturn(patients);

        List<PatientDTO> result = patientService.searchByBirthDateRange(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getBirthDate().isAfter(startDate.minusDays(1)));
        assertTrue(result.get(0).getBirthDate().isBefore(endDate.plusDays(1)));

        verify(patientRepository, times(1)).findByBirthDateBetween(startDate, endDate);
    }
}