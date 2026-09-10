package com.lundim.job_applications_tracker.dto.applications;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.model.enums.JobType;
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
public class JobApplicationResponse {

    private Long id;
    private String companyName;
    private String jobTitle;
    private JobType jobType;
    private String location;
    private LocalDate dateApplied;
    private ApplicationStatus status;
    private LocalDate statusUpdateDate;
}
