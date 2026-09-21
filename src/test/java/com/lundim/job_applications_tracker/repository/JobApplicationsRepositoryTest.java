package com.lundim.job_applications_tracker.repository;

import com.lundim.job_applications_tracker.model.entity.CustomUser;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.model.entity.JobApplication;
import com.lundim.job_applications_tracker.model.enums.JobType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.lundim.job_applications_tracker.testUtil.JobApplicationsData.createTestApplication;
import static com.lundim.job_applications_tracker.testUtil.UserData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class JobApplicationsRepositoryTest {

    @Autowired
    private JobApplicationsRepository jobApplicationsRepository;

    @Autowired
    private TestEntityManager entityManager;

    // CRUD

    @Test
    public void shouldSaveJobApplication(){
        CustomUser user = createUser();
        entityManager.persist(user);

        JobApplication jobApplication = createTestApplication(user,
                "Microsoft",
                "Software Developer",
                JobType.CONTRACT,
                ApplicationStatus.REJECTED,
                "Pretoria");

        JobApplication saved = jobApplicationsRepository.save(jobApplication);

        assertNotNull(saved.getId());
        assertEquals("Microsoft", saved.getCompanyName());
    }

    @Test
    public void shouldFindJobApplicationByID(){
        CustomUser user = createUser();

        JobApplication application = createAndSaveApplication(user);

        Optional<JobApplication> result = jobApplicationsRepository.findById(application.getId());

        assertTrue(result.isPresent());
        assertEquals("Microsoft", application.getCompanyName());
        assertEquals("Pretoria", application.getLocation());
    }

    @Test
    public void shouldUpdateJobApplicationStatus(){
        CustomUser user = createUser();

        JobApplication application = createAndSaveApplication(user);

        application.setStatus(ApplicationStatus.OFFER);
        application.setStatusUpdateDate(LocalDate.now());

        jobApplicationsRepository.save(application);
        Optional<JobApplication> result = jobApplicationsRepository.findById(application.getId());

        assertThat(result).isPresent();
        assertThat(result).get().isEqualTo(application);
        assertEquals( "OFFER", application.getStatus().name());
    }

    @Test
    public void shouldDeleteJobApplication(){
        CustomUser user = createUser();

        JobApplication application = createAndSaveApplication(user);

        jobApplicationsRepository.deleteById(application.getId());

        Optional<JobApplication> results = jobApplicationsRepository.findById(application.getId());

        assertThat(results).isEmpty();
    }

    // Filtering

    @Test
    public void shouldFindApplicationsByCompanyName(){
        CustomUser user = createUser();

        createAndSaveMultiApplications(user);

        Page<JobApplication> result = jobApplicationsRepository.findApplications(
                user.getId(),
                "gooGle",
                null,
                null,
                null,
                null,
                PageRequest.of(0,10)
        );


        assertThat(result).isNotEmpty();
        assertEquals(3, result.getTotalElements());
    }

    @Test
    public void shouldFindApplicationsByJobTitle() {

        CustomUser user = createUser();

        createAndSaveMultiApplications(user);

        Page<JobApplication> result = jobApplicationsRepository.findApplications(
                user.getId(),
                null,
                "IT Support",
                null,
                null,
                null,
                PageRequest.of(0,10)
        );


        assertThat(result).isNotEmpty();
        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void shouldFindApplicationsByJobType() {
        CustomUser user = createUser();

        createAndSaveMultiApplications(user);

        Page<JobApplication> result = jobApplicationsRepository.findApplications(
                user.getId(),
                null,
                null,
                null,
                JobType.CONTRACT,
                null,
                PageRequest.of(0,10)
        );


        assertThat(result).isNotEmpty();
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void shouldFindApplicationsByStatus() {
        CustomUser user = createUser();

        createAndSaveMultiApplications(user);

        Page<JobApplication> results = jobApplicationsRepository.findApplications(
                user.getId(),
                null,
                null,
                ApplicationStatus.APPLIED,
                null,
                null,
                PageRequest.of(0,10)
        );


        assertThat(results).isNotEmpty();
        assertEquals(2, results.getTotalElements());
    }

    @Test
    public void shouldFindApplicationsByLocation() {
        CustomUser user = createUser();

        createAndSaveMultiApplications(user);

        Page<JobApplication> result = jobApplicationsRepository.findApplications(
                user.getId(),
                null,
                null,
                null,
                null,
                "durban",
                PageRequest.of(0,10)
        );


        assertThat(result).isNotEmpty();
        assertEquals(1, result.getTotalElements());
    }

    // Combined Filters

    @Test
    public void shouldFindApplicationsByCompanyNameAndStatus() {
        CustomUser user = createUser();

        createAndSaveMultiApplications(user);

        Page<JobApplication> result = jobApplicationsRepository.findApplications(
                user.getId(),
                "google",
                null,
                ApplicationStatus.APPLIED,
                null,
                null,
                PageRequest.of(0,10)
        );

        assertThat(result).isNotEmpty();
        assertEquals(1, result.getTotalElements());

    }
    @Test
    public void shouldFindApplicationsUsingMultipleFilters() {

        CustomUser user = createUser();

        createAndSaveMultiApplications(user);

        Page<JobApplication> result = jobApplicationsRepository.findApplications(
                user.getId(),
                "google",
                null,
                ApplicationStatus.REJECTED,
                JobType.LEARNERSHIP,
                null,
                PageRequest.of(0,10)
        );


        assertThat(result).isNotEmpty();
        assertEquals(1, result.getTotalElements());
    }

    // Pagination

    @Test
    public void shouldReturnPaginatedApplications() {

        CustomUser user = createUser();
        createAndSaveMultiApplications(user);

        Page<JobApplication> result = jobApplicationsRepository.findApplications(
                user.getId(),
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0,10)
        );

        assertThat(result).isNotEmpty();
        assertEquals(4, result.getTotalElements());
    }

    @Test
    public void shouldReturnEmptyWhenNoApplicationsMatchFilters() {
        CustomUser user = createUser();

        createAndSaveMultiApplications(user);

        Page<JobApplication> result = jobApplicationsRepository.findApplications(
                100L,
                "google",
                "Software Developer",
                ApplicationStatus.fromString("applied"),
                JobType.fromString("full time"),
                "Johannesburg",
                PageRequest.of(0,10)
        );

        assertThat(result).isEmpty();
    }

    //User Isolation

    @Test
    public void shouldFindApplicationsBelongingToUser() {

        CustomUser userA = createUser();
        CustomUser userB = createUserB();

        createAndSaveApplication(userA);
        createAndSaveApplication(userB);

        Page<JobApplication> results = jobApplicationsRepository.findApplications(
                userB.getId(),
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 10)
        );

        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent()
                .stream().allMatch(jobApplication ->
                        jobApplication.getUser().getId().equals(userB.getId())));

    }

    // Helper Methods

    private JobApplication createAndSaveApplication(CustomUser user){
        entityManager.persist(user);

        JobApplication jobApplication = createTestApplication(user,
                "Microsoft",
                "Software Developer",
                JobType.CONTRACT,
                ApplicationStatus.APPLIED,
                "Pretoria");

        return jobApplicationsRepository.save(jobApplication);
    }

    private void createAndSaveMultiApplications(CustomUser user){
        entityManager.persist(user);

        JobApplication jobApplication = createTestApplication(user,
                "Google",
                "Software Developer",
                JobType.INTERNSHIP,
                ApplicationStatus.ACCEPTED,
                "Cape Town");

        JobApplication jobApplicationB = createTestApplication(user,
                "Asus",
                "Software Developer",
                JobType.LEARNERSHIP,
                ApplicationStatus.APPLIED,
                "Cape Town");

        JobApplication jobApplicationC = createTestApplication(user,
                "Google",
                "IT Support",
                JobType.CONTRACT,
                ApplicationStatus.APPLIED,
                "Johannesburg");

        JobApplication jobApplicationD = createTestApplication(user,
                "Google",
                "IT Support",
                JobType.LEARNERSHIP,
                ApplicationStatus.REJECTED,
                "Durban");

        jobApplicationsRepository.saveAll(List.of(jobApplication,
                jobApplicationB,
                jobApplicationC,
                jobApplicationD));
    }
}
