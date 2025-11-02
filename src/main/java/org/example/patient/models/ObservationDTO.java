package org.example.patient.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObservationDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long encounterId;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Value is required")
    private String value;

    @NotNull(message = "Effective date time is required")
    private LocalDateTime effectiveDateTime;
}
