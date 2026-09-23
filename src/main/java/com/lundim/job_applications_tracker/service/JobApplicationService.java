package com.lundim.job_applications_tracker.service;

import com.lundim.job_applications_tracker.dto.applications.JobApplicationRequest;
import com.lundim.job_applications_tracker.dto.applications.JobApplicationResponse;
import com.lundim.job_applications_tracker.dto.applications.UpdateApplicationStatus;
import com.lundim.job_applications_tracker.model.entity.CustomUser;
import com.lundim.job_applications_tracker.model.entity.JobApplication;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.model.enums.JobType;
import com.lundim.job_applications_tracker.repository.JobApplicationsRepository;
import com.lundim.job_applications_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final UserRepository userRepository;
    private final JobApplicationsRepository repository;

    // CREATE
    public JobApplicationResponse createJobApplication(String email, JobApplicationRequest request) {

        CustomUser user = getUserByEmail(email);

        LocalDate dateApplied = request.getDateApplied() != null
                ? request.getDateApplied()
                : LocalDate.now();

        JobApplication jobApplication = JobApplication.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .jobType(JobType.fromString(request.getJobType()))
                .status(ApplicationStatus.APPLIED)
                .location(request.getLocation())
                .dateApplied(dateApplied)
                .statusUpdateDate(dateApplied)
                .build();

        JobApplication saved = repository.save(jobApplication);

        return mapToDTO(saved);
    }

    // GET ALL OR FILTER
    public Page<JobApplicationResponse> getApplications(
            String email,
            ApplicationStatus status,
            String jobTitle,
            String companyName,
            JobType jobType,
            String location,
            Pageable pageable) {

        CustomUser user = getUserByEmail(email);

        Page<JobApplication> applications =
                repository.findApplications(
                        user.getId(),
                        companyName,
                        jobTitle,
                        status,
                        jobType,
                        location,
                        pageable
                );

        return applications.map(this::mapToDTO);
    }

    // GET ONE
    public JobApplicationResponse getJobApplication(String email, Long id) {

        CustomUser user = getUserByEmail(email);

        JobApplication jobApplication = getUserApplication(user, id);

        return mapToDTO(jobApplication);
    }

    // UPDATE STATUS
    public JobApplicationResponse updateJobStatus(
            String email,
            Long id,
            UpdateApplicationStatus updateApplicationStatus) {

        CustomUser user = getUserByEmail(email);

        JobApplication job = getUserApplication(user, id);

        ApplicationStatus status =
                ApplicationStatus.fromString(
                        updateApplicationStatus.getStatus()
                );

        job.setStatus(status);
        job.setStatusUpdateDate(LocalDate.now());

        JobApplication saved = repository.save(job);

        return mapToDTO(saved);
    }

    // DELETE
    public void deleteJobApplication(String email, Long id) {

        CustomUser user = getUserByEmail(email);

        JobApplication jobApplication = getUserApplication(user, id);

        repository.delete(jobApplication);
    }

    // HELPER METHODS

    // FIND USER
    private CustomUser getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }

    // FIND APPLICATION BELONGING TO USER
    private JobApplication getUserApplication(
            CustomUser user,
            Long id) {

        JobApplication jobApplication =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Job Application not found."));

        if (!jobApplication.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Job Application not found.");
        }

        return jobApplication;
    }

    // MAPPER
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