package org.ngo.dashboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET / should return HTTP 200 and display Dashboard")
    void testDashboardPageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("metrics"))
                .andExpect(model().attributeExists("projects"))
                .andExpect(content().string(containsString("NGO Project Dashboard")));
    }

    @Test
    @DisplayName("GET /projects should display project listing")
    void testProjectListLoads() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("project-list"))
                .andExpect(model().attributeExists("projects"))
                .andExpect(content().string(containsString("Community Initiatives")));
    }

    @Test
    @DisplayName("GET /projects/new should display empty project registration form")
    void testCreateFormLoads() throws Exception {
        mockMvc.perform(get("/projects/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("project-form"))
                .andExpect(model().attributeExists("project"))
                .andExpect(content().string(containsString("Register New NGO Initiative")));
    }

    @Test
    @DisplayName("GET /health should return 200 OK and JSON health payload")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("ngo-project-dashboard"))
                .andExpect(jsonPath("$.systemStatus").value("OPERATIONAL"));
    }

    @Test
    @DisplayName("POST /projects/save should validate form and redirect on success")
    void testSaveProjectValid() throws Exception {
        mockMvc.perform(post("/projects/save")
                        .param("name", "Solar Water Pump Project")
                        .param("description", "Deploying pumps for irrigation")
                        .param("location", "Dhar, MP")
                        .param("beneficiaryCount", "1200")
                        .param("budget", "18000.00")
                        .param("spent", "2000.00")
                        .param("status", "IN_PROGRESS")
                        .param("priority", "HIGH")
                        .param("progressPercent", "15")
                        .param("leadOfficer", "Suresh Patel")
                        .param("startDate", "2026-02-01")
                        .param("targetEndDate", "2026-08-30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }
}
