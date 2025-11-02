package org.example.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.patient.models.EncounterDTO;
import org.example.patient.models.ObservationDTO;
import org.example.patient.models.PatientDTO;
import org.example.patient.service.EncounterService;
import org.example.patient.service.ObservationService;
import org.example.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@Tag(name = "Patient", description = "Patient management endpoints")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private EncounterService encounterService;

    @Autowired
    private ObservationService observationService;

    @PostMapping
    @Operation(summary = "Create a new patient", description = "Creates a new patient record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<PatientDTO>> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO created = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(
                org.example.patient.models.ApiResponse.created("Patient created successfully", created),
                HttpStatus.CREATED
        );
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get patient by id", description = "Get patient record by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<PatientDTO>> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(patient -> new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.success("Patient fetched successfully", patient),
                        HttpStatus.OK
                ))
                .orElse(new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.notFound("Patient not found"),
                        HttpStatus.NOT_FOUND
                ));
    }


    @GetMapping
    @Operation(summary = "Search patients by different filters", description = "Search patients by different filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<List<PatientDTO>>> searchPatients(
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String given,
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate) {

        List<PatientDTO> patients;

        if (family == null && given == null && identifier == null && birthDate == null) {
            patients = patientService.getAllPatients();
        }
        else if (identifier != null) {
            patients = patientService.searchByIdentifier(identifier)
                    .map(List::of)
                    .orElse(List.of());
        }
        else if (birthDate != null) {
            patients = patientService.searchByBirthDate(birthDate);
        }
        else if (family != null) {
            patients = patientService.searchByFamilyName(family);
        }
        else if (given != null) {
            patients = patientService.searchByGivenName(given);
        }
        else {
            patients = patientService.getAllPatients();
        }

        return new ResponseEntity<>(
                org.example.patient.models.ApiResponse.success("Patients fetched successfully", patients),
                HttpStatus.OK
        );
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update patient by id", description = "Update patient record by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<PatientDTO>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientDTO patientDTO) {
        return patientService.updatePatient(id, patientDTO)
                .map(patient -> new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.success("Patient updated successfully", patient),
                        HttpStatus.OK
                ))
                .orElse(new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.notFound("Patient not found"),
                        HttpStatus.NOT_FOUND
                ));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient by id", description = "Delete patient record by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        if (patientService.deletePatient(id)) {
            return new ResponseEntity<>(
                    org.example.patient.models.ApiResponse.success("Patient deleted successfully", null),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                org.example.patient.models.ApiResponse.notFound("Patient not found"),
                HttpStatus.NOT_FOUND
        );
    }


    @GetMapping("/{id}/encounters")
    @Operation(summary = "Get patient encounters by id", description = "Get patient encounters by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient encounters fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<List<EncounterDTO>>> getPatientEncounters(@PathVariable Long id) {
        if (patientService.getPatientById(id).isEmpty()) {
            return new ResponseEntity<>(
                    org.example.patient.models.ApiResponse.notFound("Patient not found"),
                    HttpStatus.NOT_FOUND
            );
        }

        List<EncounterDTO> encounters = encounterService.getEncountersByPatientId(id);
        return new ResponseEntity<>(
                org.example.patient.models.ApiResponse.success("Patient encounters fetched successfully", encounters),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/observations")
    @Operation(summary = "Get patient observations by id", description = "Get patient observations by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient observations fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<List<ObservationDTO>>> getPatientObservations(@PathVariable Long id) {
        if (patientService.getPatientById(id).isEmpty()) {
            return new ResponseEntity<>(
                    org.example.patient.models.ApiResponse.notFound("Patient not found"),
                    HttpStatus.NOT_FOUND
            );
        }

        List<ObservationDTO> observations = observationService.getObservationsByPatientId(id);
        return new ResponseEntity<>(
                org.example.patient.models.ApiResponse.success("Patient observations fetched successfully", observations),
                HttpStatus.OK
        );
    }
}