package com.lundim.job_applications_tracker.dto;

import com.lundim.job_applications_tracker.model.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobApplicationDTO {

    @NotBlank
    private String companyName;

    @NotBlank
    private String jobTitle;

    @NotNull
    private JobType jobType;

    @NotBlank
    private String location;

    @PastOrPresent(message = "Date applied cannot be in the future")
    private LocalDate dateApplied; // optional

}
