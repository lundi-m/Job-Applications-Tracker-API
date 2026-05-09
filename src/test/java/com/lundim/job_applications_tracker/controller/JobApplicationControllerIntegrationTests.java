package com.lundim.job_applications_tracker.controller;

import com.lundim.job_applications_tracker.dto.CreateJobApplicationDTO;
import com.lundim.job_applications_tracker.dto.UpdateApplicationStatusDTO;
import com.lundim.job_applications_tracker.repository.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class JobApplicationControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object obj){
        return objectMapper.writeValueAsString(obj);
    }

    private Long createApplicationAndGetApplicationId(CreateJobApplicationDTO dto) throws Exception {
        String response = mockMvc.perform(post("/job-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    public void testThatCreateJobApplicationReturnsStatus201Created() throws Exception {
        CreateJobApplicationDTO request = TestDataUtil.createTestRequest();

        mockMvc.perform(post("/job-applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void testThatListJobApplicationsReturnsStatus200OK() throws Exception {
        mockMvc.perform(get("/job-applications"))
                .andExpect(status().isOk());
    }

    @Test
    public void testThatListJobApplicationsReturnsApplications() throws Exception {
        CreateJobApplicationDTO requestA = TestDataUtil.createTestRequest();
        CreateJobApplicationDTO requestB = TestDataUtil.createTestRequestB();

        createApplicationAndGetApplicationId(requestA);
        createApplicationAndGetApplicationId(requestB);

        mockMvc.perform(get("/job-applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testThatGetByIdReturnsJobApplication() throws Exception {
        CreateJobApplicationDTO application = TestDataUtil.createTestRequestB();
        Long id = createApplicationAndGetApplicationId(application);

        mockMvc.perform(get("/job-applications/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobTitle").value(application.getJobTitle()));
    }

    @Test
    public void testThatGetByIdReturnsHttpStatus404ForExistingApplication() throws Exception {
        mockMvc.perform(get("/job-applications/{id}", 99999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testThatGetByStatusReturnJobApplications() throws Exception {

        CreateJobApplicationDTO application = TestDataUtil.createTestRequestB();
        createApplicationAndGetApplicationId(application);

        mockMvc.perform(get("/job-applications/status/{status}", "APPLIED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value(application.getCompanyName()));
    }

    @Test
    public void testThatGetByCompanyNameReturnJobApplications() throws Exception {
        CreateJobApplicationDTO application = TestDataUtil.createTestRequest();
        createApplicationAndGetApplicationId(application);

        mockMvc.perform(get("/job-applications/company/{company}", application.getCompanyName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobTitle").value(application.getJobTitle()));
    }

    @Test
    public void testThatGetByJobTitleReturnJobApplications() throws Exception {
        CreateJobApplicationDTO application = TestDataUtil.createTestRequest();
        createApplicationAndGetApplicationId(application);

        mockMvc.perform(get("/job-applications/job-title/{title}", application.getJobTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobTitle").value(application.getJobTitle()));
    }

    @Test
    public void testThatGetByJobTypeReturnJobApplications() throws Exception {
        CreateJobApplicationDTO application = TestDataUtil.createTestRequest();
        createApplicationAndGetApplicationId(application);

        mockMvc.perform(get("/job-applications/job-type/{type}", application.getJobType()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value(application.getCompanyName()));
    }

    @Test
    public void testThatGetByLocationReturnJobApplications() throws Exception {
        CreateJobApplicationDTO application = TestDataUtil.createTestRequest();
        createApplicationAndGetApplicationId(application);

        mockMvc.perform(get("/job-applications/location/{location}", application.getLocation()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value(application.getCompanyName()))
                .andExpect(jsonPath("$[0].jobTitle").value(application.getJobTitle()));
    }

    @Test
    public void testThatJobApplicationStatusCanBeUpdated() throws Exception {
        CreateJobApplicationDTO application = TestDataUtil.createTestRequest();
        Long id = createApplicationAndGetApplicationId(application);

        UpdateApplicationStatusDTO update = new UpdateApplicationStatusDTO();
        update.setStatus("Interview");

        mockMvc.perform(patch("/job-applications/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INTERVIEW"));
    }

    @Test
    public void testThatDeleteJobApplicationReturnHttpStatus204ForNonExistingApplication() throws Exception {
        mockMvc.perform(delete("/job-applications/{id}", 999999))
                .andExpect(status().isNoContent());
    }
}