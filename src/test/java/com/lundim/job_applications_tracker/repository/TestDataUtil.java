package com.lundim.job_applications_tracker.repository;

import com.lundim.job_applications_tracker.dto.applications.CreateJobApplication;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.model.entity.JobApplication;
import com.lundim.job_applications_tracker.model.enums.JobType;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class TestDataUtil {

    public static JobApplication createTestApplication() {
        return JobApplication.builder()
                .companyName("Microsoft")
                .jobTitle("Software Developer")
                .jobType(JobType.CONTRACT)
                .location("Pretoria")
                .build();
    }

    public static JobApplication createTestApplicationB(){
        return JobApplication.builder()
                .companyName("FNB")
                .dateApplied(LocalDate.of(2026, 3, 1))
                .jobTitle("Front-end Developer")
                .jobType(JobType.INTERNSHIP)
                .location("Springs")
                .build();
    }

    public static JobApplication createTestApplicationC(){
        return JobApplication.builder()
                .companyName("PPC")
                .dateApplied(LocalDate.of(2026,2,14))
                .jobTitle("Project Manager")
                .jobType(JobType.FULL_TIME)
                .location("Johannesburg")
                .status(ApplicationStatus.INTERVIEW)
                .build();
    }

    public static JobApplication createTestApplicationD(){
        return JobApplication.builder()
                .companyName("EY")
                .dateApplied(LocalDate.of(2026,2,14))
                .jobTitle("Junior Software Developer")
                .jobType(JobType.FULL_TIME)
                .location("Remote")
                .status(ApplicationStatus.ACCEPTED)
                .statusUpdateDate(LocalDate.of(2026, 3, 5))
                .build();
    }

    public static CreateJobApplication createTestRequest() {
        return CreateJobApplication.builder()
                .companyName("Hisense")
                .jobTitle("Developer")
                .jobType("Part time")
                .location("Remote")
                .dateApplied(LocalDate.of(2026, 3, 21))
                .build();
    }

    public static CreateJobApplication createTestRequestB() {
        return CreateJobApplication.builder()
                .companyName("Hisense")
                .jobTitle("IT Support")
                .jobType("FULL TIME")
                .location("Springs")
                .build();
    }
}
