package org.example.patient.models;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncounterDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the future or present")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in the future or present")
    private LocalDate endDate;

    @NotBlank(message = "Encounter class is required")
    private String encounterClass;
}
