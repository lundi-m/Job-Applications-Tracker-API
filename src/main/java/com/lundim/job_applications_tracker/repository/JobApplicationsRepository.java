package com.lundim.job_applications_tracker.repository;

import com.lundim.job_applications_tracker.model.ApplicationStatus;
import com.lundim.job_applications_tracker.model.JobApplication;
import com.lundim.job_applications_tracker.model.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationsRepository extends JpaRepository<JobApplication, Long> {


    List<JobApplication> findByStatus(ApplicationStatus status);

    List<JobApplication> findByJobTitle(String jobTitle);

    List<JobApplication> findByCompanyNameIgnoreCase(String companyName);

    List<JobApplication> findByJobType(JobType jobType);

    List<JobApplication> findByLocationIgnoreCase(String location);
}
