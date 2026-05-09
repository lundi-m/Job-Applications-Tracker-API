package com.lundim.job_applications_tracker.model;

public enum ApplicationStatus {
    APPLIED,
    INTERVIEW,
    REJECTED,
    OFFER,
    ACCEPTED;

    public static ApplicationStatus fromString(String userStatus){

        for (ApplicationStatus status : values()){
            if (status.name().equalsIgnoreCase(userStatus)){
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Status: " + userStatus);
    }
}