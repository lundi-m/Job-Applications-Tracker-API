package com.lundim.job_applications_tracker.controller;


import com.lundim.job_applications_tracker.dto.applications.JobApplicationRequest;
import com.lundim.job_applications_tracker.dto.applications.JobApplicationResponse;
import com.lundim.job_applications_tracker.dto.applications.UpdateApplicationStatus;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.model.enums.JobType;
import com.lundim.job_applications_tracker.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;

    @PostMapping
    public ResponseEntity<JobApplicationResponse> createJobApplication(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        JobApplicationResponse response = service.createJobApplication(userDetails.getUsername(), request);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<JobApplicationResponse>> getApplications(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) String location,
            Pageable pageable) {
        return ResponseEntity.ok(service.getApplications(userDetails.getUsername(),
                status,
                jobTitle,
                companyName,
                jobType,
                location,
                pageable
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getJobApplication(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        JobApplicationResponse response = service.getJobApplication(userDetails.getUsername(), id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponse> updateJobStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatus request) {

        JobApplicationResponse response = service.updateJobStatus(userDetails.getUsername(), id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplication(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        service.deleteJobApplication(userDetails.getUsername(), id);

        return ResponseEntity.noContent().build();
    }
}