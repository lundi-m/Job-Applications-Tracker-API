package com.lundim.job_applications_tracker.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({
        "id",
        "firstName",
        "lastName",
        "email"
})
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
