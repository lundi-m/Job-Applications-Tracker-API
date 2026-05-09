package com.lundim.job_applications_tracker.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lundim.job_applications_tracker.model.ApplicationStatus;
import com.lundim.job_applications_tracker.model.JobType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonPropertyOrder({
        "id",
        "companyName",
        "jobTitle",
        "jobType",
        "location",
        "status",
        "dateApplied",
        "statusUpdateDate"
})
public class JobApplicationResponseDTO {

    private Long id;
    private String companyName;
    private String jobTitle;
    private JobType jobType;
    private String location;
    private LocalDate dateApplied;
    private ApplicationStatus status;
    private LocalDate statusUpdateDate;
}
