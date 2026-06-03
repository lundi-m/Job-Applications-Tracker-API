package com.lundim.job_applications_tracker.controller;


import com.lundim.job_applications_tracker.dto.*;
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
    public ResponseEntity<JobApplicationResponseDTO> createJobApplication(
            @Valid @RequestBody CreateJobApplicationDTO dto
    ) {
        JobApplicationResponseDTO response = service.createJobApplication(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponseDTO> getJobApplicationById(@PathVariable Long id) {
        JobApplicationResponseDTO jobApplicationById = service.getJobApplicationById(id);
        return ResponseEntity.ok(jobApplicationById);
    }
    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDTO>> getAllJobApplications() {
        List<JobApplicationResponseDTO> jobApplications = service.getAllJobApplications();
        return ResponseEntity.ok(jobApplications);
    }

    @GetMapping("/company/{companyName}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(service.getByCompanyName(companyName));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    @GetMapping("/job-type/{jobType}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getByJobType(@PathVariable String jobType) {
        return ResponseEntity.ok(service.getByJobType(jobType));
    }

    @GetMapping("/job-title/{jobTitle}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getByJobTitle(@PathVariable String jobTitle) {
        return ResponseEntity.ok(service.getByJobTitle(jobTitle));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getByLocation(@PathVariable String location) {
        return ResponseEntity.ok(service.getByLocation(location));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponseDTO> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateApplicationStatusDTO dto) {
        JobApplicationResponseDTO updatedJobApplication = service.updateJobStatus(id, dto);
        return ResponseEntity.ok(updatedJobApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable Long id) {
        service.deleteJobApplication(id);
        return ResponseEntity.noContent().build();
    }
}