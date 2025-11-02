package org.example.patient.models;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    @NotBlank(message = "Identifier is required")
    private String identifier;

    @NotBlank(message = "Given name is required")
    private String givenName;

    @NotBlank(message = "Family name is required")
    private String familyName;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "MALE|FEMALE", message = "Invalid gender value")
    private String gender;
}
