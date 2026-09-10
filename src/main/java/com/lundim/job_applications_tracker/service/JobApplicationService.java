package com.lundim.job_applications_tracker.service;

import com.lundim.job_applications_tracker.dto.applications.CreateJobApplication;
import com.lundim.job_applications_tracker.dto.applications.JobApplicationResponse;
import com.lundim.job_applications_tracker.dto.applications.UpdateApplicationStatus;
import com.lundim.job_applications_tracker.exception.ResourceNotFoundException;
import com.lundim.job_applications_tracker.model.entity.CustomUser;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.model.entity.JobApplication;
import com.lundim.job_applications_tracker.model.enums.JobType;
import com.lundim.job_applications_tracker.repository.JobApplicationsRepository;
import com.lundim.job_applications_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final UserRepository userRepository;
    private final JobApplicationsRepository repository;

    public JobApplicationResponse createJobApplication(String email, CreateJobApplication dto) {

        CustomUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        LocalDate dateApplied = dto.getDateApplied() != null ? dto.getDateApplied() : LocalDate.now();

        JobApplication jobApplication = JobApplication.builder()
                .user(user)
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

    public Page<JobApplicationResponse> getApplications(String email,
                                                        ApplicationStatus status,
                                                        String jobTitle,
                                                        String companyName,
                                                        JobType jobType,
                                                        String location,
                                                        Pageable pageable) {

        CustomUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        Page<JobApplication> applications = repository.findApplications(user.getId(),
                        companyName,
                        jobTitle,
                        status,
                        jobType,
                        location,
                        pageable);

        return applications.map(this::mapToDTO);
    }

    public JobApplicationResponse updateJobStatus(Long id, UpdateApplicationStatus dto) {
        JobApplication job = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job Application not found."));

        ApplicationStatus status = ApplicationStatus.fromString(dto.getStatus());
        job.setStatus(status);
        job.setStatusUpdateDate(LocalDate.now());

        return mapToDTO(repository.save(job));
    }

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