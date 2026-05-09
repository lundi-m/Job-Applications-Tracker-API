package com.lundim.job_applications_tracker.repository;

import com.lundim.job_applications_tracker.model.ApplicationStatus;
import com.lundim.job_applications_tracker.model.JobApplication;
import com.lundim.job_applications_tracker.model.JobType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class JobApplicationsRepositoryIntegrationTests {

    @Autowired
    private  JobApplicationsRepository undertest;


    @Test
    public void testThatJobApplicationCanBeCreatedAndRecalled(){

        JobApplication jobApplication = TestDataUtil.createTestApplicationB();
        undertest.save(jobApplication);
        Optional<JobApplication> result = undertest.findById(jobApplication.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getCompanyName()).isEqualTo(jobApplication.getCompanyName());
    }

    @Test
    public void testThatJobApplicationCanBeCreatedAndRecalledByCompanyName(){

        JobApplication jobApplication = TestDataUtil.createTestApplicationB();
        undertest.save(jobApplication);
        List<JobApplication> result = undertest.findByCompanyNameIgnoreCase(jobApplication.getCompanyName());
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(jobApplication.getId());
    }

    @Test
    public void testThatJobApplicationCanBeCreatedAndRecalledByApplicationsStatus(){

        JobApplication jobApplication = TestDataUtil.createTestApplicationC();
        undertest.save(jobApplication);
        List<JobApplication> result = undertest.findByStatus(jobApplication.getStatus());
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(jobApplication.getId());
    }

    @Test
    public void testThatJobApplicationCanBeCreatedAndRecalledByLocation() {
        JobApplication jobApplication = TestDataUtil.createTestApplicationD();
        undertest.save(jobApplication);
        List<JobApplication> result = undertest.findByLocationIgnoreCase(jobApplication.getLocation());
        assertThat(result).isNotEmpty();
    }

    @Test
    public void testThatJobApplicationCanBeCreatedAndRecalledByJobType(){

        JobApplication jobApplication = TestDataUtil.createTestApplication();
        undertest.save(jobApplication);// Job Type = Contract

        JobApplication jobApplicationB = TestDataUtil.createTestApplicationB();
        undertest.save(jobApplicationB);// Job Type = Internship

        JobApplication jobApplicationC = TestDataUtil.createTestApplicationC();
        undertest.save(jobApplicationC);// Job Type = Full time

        JobApplication jobApplicationD = TestDataUtil.createTestApplicationD();
        undertest.save(jobApplicationD);// Job Type = Full Time

        Iterable<JobApplication> results = undertest.findByJobType(JobType.FULL_TIME);
        assertThat(results)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(jobApplicationC, jobApplicationD);
    }

    @Test
    public void testThatMultipleJobApplicationsCanBeCreatedAndRecalled(){

        JobApplication jobApplication = TestDataUtil.createTestApplication();
        undertest.save(jobApplication);

        JobApplication jobApplicationB = TestDataUtil.createTestApplicationB();
        undertest.save(jobApplicationB);

        JobApplication jobApplicationC = TestDataUtil.createTestApplicationC();
        undertest.save(jobApplicationC);

        JobApplication jobApplicationD = TestDataUtil.createTestApplicationD();
        undertest.save(jobApplicationD);

        Iterable<JobApplication> results = undertest.findAll();

        assertThat(results)
                .hasSize(4)
                .containsExactly(jobApplication, jobApplicationB,jobApplicationC, jobApplicationD);
    }

    @Test
    public void testThatStatusCanBeUpdated(){

        JobApplication jobApplication = TestDataUtil.createTestApplicationB();
        undertest.save(jobApplication);

        jobApplication.setStatus(ApplicationStatus.OFFER);
        jobApplication.setStatusUpdateDate(LocalDate.now());
        undertest.save(jobApplication);
        Optional<JobApplication> result = undertest.findById(jobApplication.getId());
        assertThat(result).isPresent();
        assertThat(result).get().isEqualTo(jobApplication);
    }

    @Test
    public void testThatJobApplicationCanDeleted(){
        JobApplication jobApplication = TestDataUtil.createTestApplicationB();
        undertest.save(jobApplication);

        undertest.deleteById(jobApplication.getId());
        Optional<JobApplication> results = undertest.findById(jobApplication.getId());
        assertThat(results).isEmpty();
    }
}
