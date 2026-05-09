package com.lundim.job_applications_tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_applications")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
            generator = "job_applications_id_sequence")
    private Long id;

    private String companyName;
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private String location;
    private LocalDate dateApplied;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDate statusUpdateDate;

}