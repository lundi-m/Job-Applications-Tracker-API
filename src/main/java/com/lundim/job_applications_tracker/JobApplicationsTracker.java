package com.lundim.job_applications_tracker;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobApplicationsTracker {

	final DataSource dataSource;

    public JobApplicationsTracker(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public static void main(String[] args) {
		SpringApplication.run(JobApplicationsTracker.class, args);
	}

}
