package com.lundim.job_applications_tracker.testUtil;

import com.lundim.job_applications_tracker.dto.applications.JobApplicationRequest;
import com.lundim.job_applications_tracker.dto.applications.JobApplicationResponse;
import com.lundim.job_applications_tracker.model.entity.CustomUser;
import com.lundim.job_applications_tracker.model.entity.JobApplication;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.model.enums.JobType;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public class JobApplicationsData {

    public static JobApplication createTestApplication(CustomUser user,
                                                       String companyName,
                                                       String jobTitle,
                                                       JobType jobType,
                                                       ApplicationStatus status,
                                                       String location) {
        return JobApplication.builder()
                .user(user)
                .companyName(companyName)
                .jobTitle(jobTitle)
                .jobType(jobType)
                .status(status)
                .location(location)
                .build();
    }

    public static JobApplicationRequest createApplicationRequest() {
        return JobApplicationRequest.builder()
                .companyName("Google")
                .jobTitle("Developer")
                .jobType("Part time")
                .location("Remote")
                .dateApplied(LocalDate.of(2026, 3, 21))
                .build();
    }

    public static JobApplicationRequest createApplicationRequestB() {
        return JobApplicationRequest.builder()
                .companyName("Asus")
                .jobTitle("IT Support")
                .jobType("FULL TIME")
                .location("Springs")
                .build();
    }

    public static JobApplicationResponse createApplicationResponse(){
        return JobApplicationResponse.builder()
                .id(1L)
                .companyName("Google")
                .jobTitle("Developer")
                .jobType(JobType.fromString("Part time"))
                .location("Remote")
                .dateApplied(LocalDate.now())
                .status(ApplicationStatus.APPLIED)
                .statusUpdateDate(LocalDate.now())
                .build();
    }

    public static JobApplicationResponse createApplicationResponseB(){
        return JobApplicationResponse.builder()
                .id(121L)
                .companyName("Asus")
                .jobTitle("IT Support")
                .jobType(JobType.fromString("FULL TIME"))
                .location("Springs")
                .dateApplied(LocalDate.now())
                .status(ApplicationStatus.REJECTED)
                .statusUpdateDate(LocalDate.now().plusDays(7))
                .build();
    }
}
