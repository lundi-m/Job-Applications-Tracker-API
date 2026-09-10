package com.lundim.job_applications_tracker.repository;

import com.lundim.job_applications_tracker.dto.applications.JobApplicationResponse;
import com.lundim.job_applications_tracker.model.entity.CustomUser;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.model.entity.JobApplication;
import com.lundim.job_applications_tracker.model.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface JobApplicationsRepository extends JpaRepository<JobApplication, Long> {


    List<JobApplication> findByStatus(ApplicationStatus status);

    List<JobApplication> findByJobTitle(String jobTitle);

    List<JobApplication> findByCompanyNameIgnoreCase(String companyName);

    List<JobApplication> findByJobType(JobType jobType);

    List<JobApplication> findByLocationIgnoreCase(String location);

    @Query("""
        SELECT j
        FROM JobApplication j
        WHERE j.user.id = :userId
            AND (:companyName IS NULL OR j.companyName ILIKE CONCAT('%', CAST(:companyName AS string), '%'))
            AND (:jobTitle IS NULL OR j.jobTitle ILIKE CONCAT('%', CAST(:jobTitle AS string), '%'))
            AND (:status IS NULL OR j.status = :status)
            AND (:jobType IS NULL OR j.jobType = :jobType)
            AND (:location IS NULL OR j.location ILIKE CONCAT('%', CAST(:location AS string), '%'))
        """)
    Page<JobApplication> findApplications(
            @Param("userId") Long userId,
            @Param("companyName") String companyName,
            @Param("jobTitle") String jobTitle,
            @Param("status") ApplicationStatus status,
            @Param("jobType") JobType jobType,
            @Param("location") String location,
            Pageable pageable
    );
}
