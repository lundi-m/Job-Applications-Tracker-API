package com.lundim.job_applications_tracker.controller;


import com.lundim.job_applications_tracker.dto.applications.CreateJobApplication;
import com.lundim.job_applications_tracker.dto.applications.JobApplicationResponse;
import com.lundim.job_applications_tracker.dto.applications.UpdateApplicationStatus;
import com.lundim.job_applications_tracker.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;

    @PostMapping
    public ResponseEntity<JobApplicationResponse> createJobApplication(
            @Valid @RequestBody CreateJobApplication dto
    ) {
        JobApplicationResponse response = service.createJobApplication(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getJobApplicationById(@PathVariable Long id) {
        JobApplicationResponse jobApplicationById = service.getJobApplicationById(id);
        return ResponseEntity.ok(jobApplicationById);
    }
    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllJobApplications() {
        List<JobApplicationResponse> jobApplications = service.getAllJobApplications();
        return ResponseEntity.ok(jobApplications);
    }

    @GetMapping("/company/{companyName}")
    public ResponseEntity<List<JobApplicationResponse>> getByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(service.getByCompanyName(companyName));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApplicationResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    @GetMapping("/job-type/{jobType}")
    public ResponseEntity<List<JobApplicationResponse>> getByJobType(@PathVariable String jobType) {
        return ResponseEntity.ok(service.getByJobType(jobType));
    }

    @GetMapping("/job-title/{jobTitle}")
    public ResponseEntity<List<JobApplicationResponse>> getByJobTitle(@PathVariable String jobTitle) {
        return ResponseEntity.ok(service.getByJobTitle(jobTitle));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<JobApplicationResponse>> getByLocation(@PathVariable String location) {
        return ResponseEntity.ok(service.getByLocation(location));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponse> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatus dto) {
        JobApplicationResponse updatedJobApplication = service.updateJobStatus(id, dto);
        return ResponseEntity.ok(updatedJobApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable Long id) {
        service.deleteJobApplication(id);
        return ResponseEntity.noContent().build();
    }
}