package com.lundim.job_applications_tracker.model.enums;

public enum JobType {
    FULL_TIME,
    PART_TIME,
    CONTRACT,
    INTERNSHIP,
    LEARNERSHIP;

    public static JobType fromString(String userJobType){

        if (userJobType == null || userJobType.trim().isEmpty()){
            throw new IllegalArgumentException("Job Type cannot be empty.");
        }

        for (JobType jobType : values()){
            if (jobType.name().equalsIgnoreCase(userJobType.replace(" ", "_"))){
                return jobType;
            }
        }
        throw new IllegalArgumentException("Invalid Job Type : " + userJobType);
    }
}
