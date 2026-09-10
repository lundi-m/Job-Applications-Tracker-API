package com.lundim.job_applications_tracker.dto.applications;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobApplication {

    @NotBlank
    private String companyName;

    @NotBlank
    private String jobTitle;

    @NotNull
    private String jobType;

    @NotBlank
    private String location;

    @PastOrPresent(message = "Date applied cannot be in the future")
    private LocalDate dateApplied; // optional

}
