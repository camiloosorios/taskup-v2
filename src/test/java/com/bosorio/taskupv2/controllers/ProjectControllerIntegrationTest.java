package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.repositories.ProjectRepository;
import com.bosorio.taskupv2.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    private Project savedProject;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/projects";

    @BeforeEach
    public void setUp() {
        projectRepository.deleteAll();
        Project project = Project.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(Arrays.asList())
                .build();

        savedProject = projectRepository.save(project);
    }

    @AfterEach
    public void tearDown() {
        projectRepository.deleteAll();
    }


    @Test
    @DisplayName("Test create a new project")
    public void createNewProject() throws Exception {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(Arrays.asList())
                .build();

        String jsonProject = objectMapper.writeValueAsString(projectDTO);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProject))
                .andExpect(status().isCreated())
                .andExpect(content().string("Project created successfully"));

    }

    @Test
    @DisplayName("Test get all projects")
    public void getAllProjects() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Test get project by id")
    public void getProjectById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" +savedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projectName").value("Test Project"))
                .andExpect(jsonPath("$.clientName").value("Test Client"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    @DisplayName("Test update project")
    public void updateProject() throws Exception {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .projectName("Test Project Updated")
                .clientName("Test Client Updated")
                .description("Test Description Updated")
                .tasks(Arrays.asList())
                .build();

        String jsonProject = objectMapper.writeValueAsString(projectDTO);

        mockMvc.perform(put(BASE_URL + "/" + savedProject.getId())
                        .content(jsonProject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(content().string("Project updated successfully"));
    }

    @Test
    @DisplayName("Test delete project")
    public void deleteProject() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + savedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Project deleted successfully"));
    }

}
