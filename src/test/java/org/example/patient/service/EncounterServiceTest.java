package org.example.patient.service;

import org.example.patient.entity.Encounter;
import org.example.patient.entity.Patient;
import org.example.patient.models.EncounterDTO;
import org.example.patient.repository.EncounterRepo;
import org.example.patient.repository.PatientRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Encounter Service Tests")
class EncounterServiceTest {

    @Mock
    private EncounterRepo encounterRepository;

    @Mock
    private PatientRepo patientRepository;

    @InjectMocks
    private EncounterService encounterService;

    private Patient patient;
    private Encounter encounter;
    private EncounterDTO encounterDTO;

    @BeforeAll
    static void setupTests() {
        System.out.println("\n========================================");
        System.out.println("   STARTING ENCOUNTER SERVICE TESTS");
        System.out.println("========================================\n");
    }

    @AfterAll
    static void tearDownTests() {
        System.out.println("\n========================================");
        System.out.println("   ALL TESTS COMPLETED SUCCESSFULLY ✅");
        System.out.println("========================================\n");
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println("▶ Running: " + testInfo.getDisplayName());

        patient = new Patient();
        patient.setId(1L);
        patient.setIdentifier("MRN-12345");
        patient.setGivenName("John");
        patient.setFamilyName("Doe");

        encounter = new Encounter();
        encounter.setId(1L);
        encounter.setPatient(patient);
        encounter.setStartDate(LocalDate.of(2025, 12, 1));
        encounter.setEndDate(LocalDate.of(2025, 12, 1));
        encounter.setEncounterClass(Encounter.EncounterClass.OUTPATIENT);

        encounterDTO = new EncounterDTO(
                1L,
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 1),
                "OUTPATIENT"
        );
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        System.out.println("✅ Passed: " + testInfo.getDisplayName() + "\n");
    }

    @Test
    @DisplayName("Test 1: Should create encounter successfully")
    void testCreateEncounter() {

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(encounterRepository.save(any(Encounter.class))).thenReturn(encounter);

        Optional<EncounterDTO> result = encounterService.createEncounter(encounterDTO);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getPatientId());
        assertEquals("OUTPATIENT", result.get().getEncounterClass());
        assertEquals( LocalDate.of(2025, 12, 1), result.get().getStartDate());
        assertEquals( LocalDate.of(2025, 12, 1), result.get().getEndDate());

        System.out.println("   - Encounter created for patient ID: " + result.get().getPatientId());
        System.out.println("   - Class: " + result.get().getEncounterClass());

        verify(patientRepository, times(1)).findById(1L);
        verify(encounterRepository, times(1)).save(any(Encounter.class));
    }

    @Test
    @DisplayName("Test 2: Should return empty when creating encounter with non-existent patient")
    void testCreateEncounter_PatientNotFound() {

        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        EncounterDTO invalidDTO = new EncounterDTO(
                999L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "OUTPATIENT"
        );

        Optional<EncounterDTO> result = encounterService.createEncounter(invalidDTO);

        assertFalse(result.isPresent());

        System.out.println("   - Correctly returned empty for non-existent patient");

        verify(patientRepository, times(1)).findById(999L);
        verify(encounterRepository, never()).save(any(Encounter.class));
    }

    @Test
    @DisplayName("Test 3: Should get encounter by ID successfully")
    void testGetEncounterById() {

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));

        Optional<EncounterDTO> result = encounterService.getEncounterById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getPatientId());
        assertEquals("OUTPATIENT", result.get().getEncounterClass());

        System.out.println("   - Found encounter ID: 1");
        System.out.println("   - Patient ID: " + result.get().getPatientId());

        verify(encounterRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test 4: Should get all encounters successfully")
    void testGetAllEncounters() {

        Encounter encounter2 = new Encounter();
        encounter2.setId(2L);
        encounter2.setPatient(patient);
        encounter2.setStartDate(LocalDate.of(2025, 12, 1));
        encounter2.setEndDate( LocalDate.of(2025, 12, 1));
        encounter2.setEncounterClass(Encounter.EncounterClass.EMERGENCY);

        List<Encounter> encounters = Arrays.asList(encounter, encounter2);
        when(encounterRepository.findAll()).thenReturn(encounters);

        List<EncounterDTO> result = encounterService.getAllEncounters();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("OUTPATIENT", result.get(0).getEncounterClass());
        assertEquals("EMERGENCY", result.get(1).getEncounterClass());

        System.out.println("   - Found " + result.size() + " encounters");
        System.out.println("   - Classes: " + result.get(0).getEncounterClass() + ", " + result.get(1).getEncounterClass());

        verify(encounterRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 5: Should get encounters by patient ID")
    void testGetEncountersByPatientId() {

        List<Encounter> encounters = Arrays.asList(encounter);
        when(encounterRepository.findByPatientId(1L)).thenReturn(encounters);

        List<EncounterDTO> result = encounterService.getEncountersByPatientId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getPatientId());

        System.out.println("   - Found " + result.size() + " encounter(s) for patient ID: 1");

        verify(encounterRepository, times(1)).findByPatientId(1L);
    }

    @Test
    @DisplayName("Test 6: Should update encounter successfully")
    void testUpdateEncounter() {
        EncounterDTO updatedDTO = new EncounterDTO(
                1L,
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 1),
                "EMERGENCY"
        );

        Encounter updatedEncounter = new Encounter();
        updatedEncounter.setId(1L);
        updatedEncounter.setPatient(patient);
        updatedEncounter.setStartDate( LocalDate.of(2025, 12, 1));
        updatedEncounter.setEndDate( LocalDate.of(2025, 12, 1));
        updatedEncounter.setEncounterClass(Encounter.EncounterClass.EMERGENCY);

        when(encounterRepository.findById(1L)).thenReturn(Optional.of(encounter));
        when(encounterRepository.save(any(Encounter.class))).thenReturn(updatedEncounter);

        Optional<EncounterDTO> result = encounterService.updateEncounter(1L, updatedDTO);

        assertTrue(result.isPresent());
        assertEquals("EMERGENCY", result.get().getEncounterClass());
        assertEquals( LocalDate.of(2025, 12, 1), result.get().getStartDate());

        System.out.println("   - Updated encounter class to: " + result.get().getEncounterClass());

        verify(encounterRepository, times(1)).findById(1L);
        verify(encounterRepository, times(1)).save(any(Encounter.class));
    }

    @Test
    @DisplayName("Test 7: Should delete encounter successfully")
    void testDeleteEncounter() {

        when(encounterRepository.existsById(1L)).thenReturn(true);
        doNothing().when(encounterRepository).deleteById(1L);

        boolean result = encounterService.deleteEncounter(1L);

        assertTrue(result);

        System.out.println("   - Successfully deleted encounter ID: 1");

        verify(encounterRepository, times(1)).existsById(1L);
        verify(encounterRepository, times(1)).deleteById(1L);
    }
}
