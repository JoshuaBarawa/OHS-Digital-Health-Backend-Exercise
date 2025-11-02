package org.example.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.patient.models.EncounterDTO;
import org.example.patient.service.EncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encounters")
@CrossOrigin(origins = "*")
@Tag(name = "Encounter", description = "Encounter management endpoints")
public class EncounterController {

    @Autowired
    private EncounterService encounterService;


    @PostMapping
    @Operation(summary = "Create new patient encounter record", description = "Create new patient encounter record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient encounter created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<EncounterDTO>> createEncounter(@Valid @RequestBody EncounterDTO encounterDTO) {
        return encounterService.createEncounter(encounterDTO)
                .map(encounter -> new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.created("Encounter created successfully", encounter),
                        HttpStatus.CREATED
                ))
                .orElse(new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.badRequest("Bad request - Patient not found"),
                        HttpStatus.BAD_REQUEST
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch patient encounter record by id", description = "Fetch patient encounter record by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient encounter fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Encounter not found")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<EncounterDTO>> getEncounterById(@PathVariable Long id) {
        return encounterService.getEncounterById(id)
                .map(encounter -> new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.success("Encounter fetched successfully", encounter),
                        HttpStatus.OK
                ))
                .orElse(new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.notFound("Encounter not found"),
                        HttpStatus.NOT_FOUND
                ));
    }

    @GetMapping
    @Operation(summary = "Fetch all patient encounters records", description = "Fetch all patient encounters records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient encounters fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<List<EncounterDTO>>> getAllEncounters() {
        List<EncounterDTO> encounters = encounterService.getAllEncounters();
        return new ResponseEntity<>(
                org.example.patient.models.ApiResponse.success("Encounters fetched successfully", encounters),
                HttpStatus.OK
        );
    }


    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Fetch patient encounter by patientId", description = "Fetch patient encounter by patientId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient encounters fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<List<EncounterDTO>>> getEncountersByPatientId(@PathVariable Long patientId) {
        List<EncounterDTO> encounters = encounterService.getEncountersByPatientId(patientId);
        return new ResponseEntity<>(
                org.example.patient.models.ApiResponse.success("Patient encounters fetched successfully", encounters),
                HttpStatus.OK
        );
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update patient encounter by id", description = "Update patient encounter by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient encounter updated successfully"),
            @ApiResponse(responseCode = "404", description = "Encounter not found")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<EncounterDTO>> updateEncounter(
            @PathVariable Long id,
            @Valid @RequestBody EncounterDTO encounterDTO) {
        return encounterService.updateEncounter(id, encounterDTO)
                .map(encounter -> new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.success("Encounter updated successfully", encounter),
                        HttpStatus.OK
                ))
                .orElse(new ResponseEntity<>(
                        org.example.patient.models.ApiResponse.notFound("Encounter not found"),
                        HttpStatus.NOT_FOUND
                ));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient encounter by id", description = "Delete patient encounter by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient encounter deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Encounter not found")
    })
    public ResponseEntity<org.example.patient.models.ApiResponse<Void>> deleteEncounter(@PathVariable Long id) {
        if (encounterService.deleteEncounter(id)) {
            return new ResponseEntity<>(
                    org.example.patient.models.ApiResponse.success("Encounter deleted successfully", null),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                org.example.patient.models.ApiResponse.notFound("Encounter not found"),
                HttpStatus.NOT_FOUND
        );
    }
}