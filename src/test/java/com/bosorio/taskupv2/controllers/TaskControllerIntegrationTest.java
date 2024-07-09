package com.bosorio.taskupv2.controllers;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.entites.Task;
import com.bosorio.taskupv2.repositories.ProjectRepository;
import com.bosorio.taskupv2.repositories.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.bosorio.taskupv2.utils.ModelConverter.projectToDTO;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/projects";

    private Task savedTask;

    private Project savedProject;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();

        Project project = Project.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(List.of())
                .build();

        savedProject = projectRepository.save(project);

        Task task = Task.builder()
                .name("Test Task")
                .description("Test Description")
                .status("pending")
                .project(savedProject)
                .build();

        savedTask = taskRepository.save(task);
    }

    @Test
    @DisplayName("Test create new task")
    public void testCreateNewTask() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder()
                .name("Test Task")
                .description("Test Description")
                .status("pending")
                .project(projectToDTO(savedProject))
                .build();

        String jsonContent = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(post(BASE_URL + "/" + savedProject.getId() + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(content().string("Task created successfully"));
    }

    @Test
    @DisplayName("Test create task without body")
    public void testCreateNewTaskWithoutBody() throws Exception {
        mockMvc.perform(post(BASE_URL + "/" + savedProject.getId() + "/tasks"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Request Body cannot be empty"));
    }

    @Test
    @DisplayName("Test create task with blank fields")
    public void testCreateNewTaskWithBlankFields() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder()
                .name("")
                .description("")
                .status("")
                .project(projectToDTO(savedProject))
                .build();

        String jsonContent = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(post(BASE_URL + "/" + savedProject.getId() + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Task Name must not be blank",
                        "Status must not be blank",
                        "Description must not be blank"
                )));
    }

    @Test
    @DisplayName("Test get all project tasks")
    public void testGetAllProjectTasks() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + savedProject.getId() + "/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Test get task by Id")
    public void testGetTaskById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + savedProject.getId() + "/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("pending"));
    }

    @Test
    @DisplayName("Test update task")
    public void testUpdateTask() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder()
                .name("Test Task Updated")
                .description("Test Description updated")
                .status("inProgress")
                .project(projectToDTO(savedProject))
                .build();

        String jsonContent = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(put(BASE_URL + "/" + savedProject.getId() + "/tasks/" + savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Task updated successfully"));
    }

    @Test
    @DisplayName("Test update task status")
    public void testUpdateTaskStatus() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder()
                .name("Test Task Updated")
                .description("Test Description updated")
                .status("underReview")
                .project(projectToDTO(savedProject))
                .build();

        String jsonContent = objectMapper.writeValueAsString(taskDTO);

        mockMvc.perform(patch(BASE_URL + "/" + savedProject.getId() + "/tasks/" + savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Task updated successfully"));
    }

    @Test
    @DisplayName("Test delete task")
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + savedProject.getId() + "/tasks/" + savedTask.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully"));
    }

}
