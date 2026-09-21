package com.lundim.job_applications_tracker.testUtil;

import com.lundim.job_applications_tracker.model.entity.CustomUser;

import java.time.Instant;

public class UserData {

    public static CustomUser createUser(){
        return CustomUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .password("LostAndFoundUnknownEncoded")
                .createdAt(Instant.now())
                .build();
    }

    public static CustomUser createUserB(){
        return CustomUser.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("janedoe@gmail.com")
                .password("LostAndFoundUnknownEncoded")
                .createdAt(Instant.now())
                .build();
    }
}
