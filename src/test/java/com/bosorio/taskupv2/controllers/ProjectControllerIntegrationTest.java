package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.entites.User;
import com.bosorio.taskupv2.repositories.ProjectRepository;
import com.bosorio.taskupv2.repositories.UserRepository;
import com.bosorio.taskupv2.services.ProjectService;
import com.bosorio.taskupv2.services.impl.ProjectServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private Project savedProject;

    private User savedUser;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/projects";

    @BeforeEach
    public void setUp() {
        projectRepository.deleteAll();
        userRepository.deleteAll();

        User user = User.builder()
                .name("John Doe")
                .email("email@email.com")
                .password("abc123")
                .confirmed(true)
                .build();

        savedUser = userRepository.save(user);

        Project project = Project.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(List.of())
                .manager(savedUser)
                .members(new HashSet<>())
                .build();

        savedProject = projectRepository.save(project);
    }

    @Test
    @DisplayName("Test create a new project")
    public void createNewProject() throws Exception {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(List.of())
                .manager(new UserDTO())
                .build();

        String jsonProject = objectMapper.writeValueAsString(projectDTO);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProject))
                .andExpect(status().isCreated())
                .andExpect(content().string("Project created successfully"));

    }

    @Test
    @DisplayName("Test create project without body")
    public void createNewProjectWithoutBody() throws Exception {
        mockMvc.perform(post(BASE_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Request Body cannot be empty"));
    }

    @Test
    @DisplayName("Test create project with blank fields")
    public void createNewProjectWithBlankFields() throws Exception {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .projectName("")
                .clientName("")
                .description("")
                .build();

        String jsonProject = objectMapper.writeValueAsString(projectDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProject))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Client Name must not be blank",
                        "Project Name must not be blank",
                        "Description must not be blank"
                )));
    }

    @Test
    @DisplayName("Test get all projects")
    public void getAllProjects() throws Exception {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(List.of())
                .build();
        when(projectService.getAllProjects()).thenReturn(List.of(projectDTO));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Test get project by id")
    public void getProjectById() throws Exception {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(List.of())
                .build();
        when(projectService.getProjectById(savedProject.getId())).thenReturn(projectDTO);

        mockMvc.perform(get(BASE_URL + "/" + savedProject.getId()))
                .andExpect(status().isOk())
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
                .tasks(List.of())
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
