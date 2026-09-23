package com.lundim.job_applications_tracker.controller;

import com.lundim.job_applications_tracker.dto.applications.JobApplicationRequest;
import com.lundim.job_applications_tracker.dto.applications.JobApplicationResponse;
import com.lundim.job_applications_tracker.dto.applications.UpdateApplicationStatus;
import com.lundim.job_applications_tracker.model.enums.ApplicationStatus;
import com.lundim.job_applications_tracker.security.JwtAuthenticationFilter;
import com.lundim.job_applications_tracker.service.JobApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static com.lundim.job_applications_tracker.testUtil.JobApplicationsData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
public class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobApplicationService applicationService;

    @MockitoBean
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object obj){
        return objectMapper.writeValueAsString(obj);
    }

    // Create

    @Test
    void shouldCreateJobApplication() throws Exception {

        when(applicationService.createJobApplication(anyString() ,any(JobApplicationRequest.class)))
                .thenReturn(createApplicationResponse());

        mockMvc.perform(post("/job-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createApplicationRequest())))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid() throws Exception {

        JobApplicationRequest request = new JobApplicationRequest();

        mockMvc.perform(post("/job-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(applicationService);
    }

    // Get all or Filtering

    @Test
    void shouldGetApplications() throws Exception{

        JobApplicationResponse response = createApplicationResponse();
        JobApplicationResponse responseB = createApplicationResponseB();

        Page<JobApplicationResponse> page = new PageImpl<>(List.of(response, responseB));

        when(applicationService.getApplications(
                anyString(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(PageRequest.class)))
                .thenReturn(page);

        mockMvc.perform(get("/job-applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    void shouldGetApplicationsWithFilters() throws Exception{

        Page<JobApplicationResponse> page = new PageImpl<>(List.of(createApplicationResponse(),
                createApplicationResponseB()));

        when(applicationService.getApplications(
                anyString(),
                eq(ApplicationStatus.fromString(("APPLIED"))),
                isNull(),
                eq("google"),
                isNull(),
                eq("remote"),
                any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/job-applications")
                        .param("companyName", "google")
                        .param("status", "APPLIED")
                        .param("location", "remote"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty());

    }

    // Get By application id

    @Test
    void shouldGetJobApplication() throws Exception {

        JobApplicationResponse response = createApplicationResponse();

        when(applicationService.getJobApplication(anyString(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(get("/job-applications/" + response.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.companyName").value("Google"));

    }


    // Update

    @Test
    void shouldUpdateJobStatus() throws Exception {

        UpdateApplicationStatus request =
                new UpdateApplicationStatus();

        request.setStatus("REJECTED");

        JobApplicationResponse response = createApplicationResponseB();

        when(applicationService.updateJobStatus(
                anyString(),
                anyLong(),
                any(UpdateApplicationStatus.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/job-applications/" + response.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.status").value("REJECTED"));

    }

    // Delete

    @Test
    void shouldDeleteJobApplication() throws Exception {

        JobApplicationResponse response = createApplicationResponse();

        mockMvc.perform(delete("/job-applications/" + response.getId()))
                .andExpect(status().isNoContent());

        verify(applicationService)
                .deleteJobApplication(anyString(), eq(response.getId()));
    }
}