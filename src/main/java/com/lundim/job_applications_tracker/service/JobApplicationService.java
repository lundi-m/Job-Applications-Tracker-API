package com.lundim.job_applications_tracker.service;

import com.lundim.job_applications_tracker.dto.applications.CreateJobApplication;
import com.lundim.job_applications_tracker.dto.applications.JobApplicationResponse;
import com.lundim.job_applications_tracker.dto.applications.UpdateApplicationStatus;
import com.lundim.job_applications_tracker.exception.ResourceNotFoundException;
import com.lundim.job_applications_tracker.model.ApplicationStatus;
import com.lundim.job_applications_tracker.model.JobApplication;
import com.lundim.job_applications_tracker.model.JobType;
import com.lundim.job_applications_tracker.repository.JobApplicationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationsRepository repository;

    // Create
    public JobApplicationResponse createJobApplication(CreateJobApplication dto) {

        LocalDate dateApplied = dto.getDateApplied() != null ? dto.getDateApplied() : LocalDate.now();

        JobApplication jobApplication = JobApplication.builder()
                .companyName(dto.getCompanyName())
                .jobTitle(dto.getJobTitle())
                .jobType(JobType.fromString(dto.getJobType()))
                .status(ApplicationStatus.APPLIED)
                .location(dto.getLocation())
                .dateApplied(dateApplied)
                .statusUpdateDate(dateApplied)
                .build();

        JobApplication saved = repository.save(jobApplication);
        return mapToDTO(saved);
    }

    // Read all
    public List<JobApplicationResponse> getAllJobApplications() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Filter by id
    public JobApplicationResponse getJobApplicationById(Long id) {
        JobApplication jobApplication = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job Application with id: " + id + " not found."
                ));
        return mapToDTO(jobApplication);
    }

    // Filter by Company Name
    public List<JobApplicationResponse> getByCompanyName(String companyName) {
        List<JobApplication> jobs = repository.findByCompanyNameIgnoreCase(companyName);
        return jobs.stream().
                map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Filter by Application Status
    public List<JobApplicationResponse> getByStatus(String status) {
        ApplicationStatus applicationStatus = ApplicationStatus.fromString(status);
        List<JobApplication> jobs = repository.findByStatus(applicationStatus);
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Filter by Job Title
    public List<JobApplicationResponse> getByJobTitle(String jobTitle) {
        List<JobApplication> jobs = repository.findByJobTitle(jobTitle);
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Filter by Job Type
    public List<JobApplicationResponse> getByJobType(String jobTypeString) {
        JobType jobType = JobType.fromString(jobTypeString);
        List<JobApplication> jobs = repository.findByJobType(jobType);
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Filter by Location
    public List<JobApplicationResponse> getByLocation(String location) {
        List<JobApplication> jobs = repository.findByLocationIgnoreCase(location);
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Update application status
    public JobApplicationResponse updateJobStatus(Long id, UpdateApplicationStatus dto) {
        JobApplication job = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Application not found."));

        ApplicationStatus status = ApplicationStatus.fromString(dto.getStatus());
        job.setStatus(status);
        job.setStatusUpdateDate(LocalDate.now());

        return mapToDTO(repository.save(job));
    }

    // Delete Job Application
    public void deleteJobApplication(Long id) {
        repository.deleteById(id);
    }

    // Mapper
    private JobApplicationResponse mapToDTO(JobApplication job) {
        return JobApplicationResponse.builder()
                .id(job.getId())
                .companyName(job.getCompanyName())
                .jobTitle(job.getJobTitle())
                .jobType(job.getJobType())
                .location(job.getLocation())
                .dateApplied(job.getDateApplied())
                .status(job.getStatus())
                .statusUpdateDate(job.getStatusUpdateDate())
                .build();
    }
}