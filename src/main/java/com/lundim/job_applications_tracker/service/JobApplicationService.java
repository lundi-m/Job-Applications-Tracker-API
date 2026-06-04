package com.lundim.job_applications_tracker.service;

import com.lundim.job_applications_tracker.dto.CreateJobApplicationDTO;
import com.lundim.job_applications_tracker.dto.JobApplicationResponseDTO;
import com.lundim.job_applications_tracker.dto.UpdateApplicationStatusDTO;
import com.lundim.job_applications_tracker.model.ApplicationStatus;
import com.lundim.job_applications_tracker.model.JobApplication;
import com.lundim.job_applications_tracker.model.JobType;
import com.lundim.job_applications_tracker.repository.JobApplicationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationsRepository repository;

    // Create
    public JobApplicationResponseDTO createJobApplication(CreateJobApplicationDTO dto) {

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
    public List<JobApplicationResponseDTO> getAllJobApplications() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Filter by id
    public JobApplicationResponseDTO getJobApplicationById(Long id) {
        JobApplication jobApplication = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job Application with id: " + id + " not found."
                ));
        return mapToDTO(jobApplication);
    }

    // Filter by Company Name
    public List<JobApplicationResponseDTO> getByCompanyName(String companyName) {
        List<JobApplication> jobs = repository.findByCompanyNameIgnoreCase(companyName);
        return jobs.stream().
                map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Filter by Application Status
    public List<JobApplicationResponseDTO> getByStatus(String status) {
        ApplicationStatus applicationStatus = ApplicationStatus.fromString(status);
        List<JobApplication> jobs = repository.findByStatus(applicationStatus);
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Filter by Job Title
    public List<JobApplicationResponseDTO> getByJobTitle(String jobTitle) {
        List<JobApplication> jobs = repository.findByJobTitle(jobTitle);
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Filter by Job Type
    public List<JobApplicationResponseDTO> getByJobType(String jobTypeString) {
        JobType jobType = JobType.fromString(jobTypeString);
        List<JobApplication> jobs = repository.findByJobType(jobType);
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Filter by Location
    public List<JobApplicationResponseDTO> getByLocation(String location) {
        List<JobApplication> jobs = repository.findByLocationIgnoreCase(location);
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Update application status
    public JobApplicationResponseDTO updateJobStatus(Long id, UpdateApplicationStatusDTO dto) {
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
    private JobApplicationResponseDTO mapToDTO(JobApplication job) {
        return JobApplicationResponseDTO.builder()
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