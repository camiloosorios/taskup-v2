package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.Exceptions.BadRequestException;
import com.bosorio.taskupv2.Exceptions.ForbbidenException;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.entites.Task;
import com.bosorio.taskupv2.repositories.ProjectRepository;
import com.bosorio.taskupv2.repositories.TaskRepository;
import com.bosorio.taskupv2.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Project project;

    private ProjectDTO projectDTO;

    private Task task;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        project = Project.builder()
                .id(1L)
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(List.of())
                .build();

        projectDTO = ProjectDTO.builder()
                .id(1L)
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(List.of())
                .build();

        task = Task.builder()
                .name("Test Task")
                .description("Test Description")
                .status("pending")
                .project(project)
                .build();
    }

    @Test
    @DisplayName("Test create task")
    public void testCreateTask() {
        TaskDTO taskDTO = TaskDTO.builder()
                .name("Test Task")
                .description("Test Description")
                .status("pending")
                .project(projectDTO)
                .build();

        assertDoesNotThrow(()-> taskService.createTask(taskDTO, projectDTO));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Test get all project tasks")
    public void testGetAllProjectTasks() {

        when(taskRepository.findByProject_Id(1L)).thenReturn(Arrays.asList(task, task));

        List<TaskDTO> tasksDTO = taskService.getProjectTasks(projectDTO);

        verify(taskRepository, times(1)).findByProject_Id(1L);
        assertEquals(2, tasksDTO.size());
    }

    @Test
    @DisplayName("Test get task by Id")
    public void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO taskDTO = taskService.getTaskById(projectDTO, 1L);

        verify(taskRepository, times(1)).findById(1L);
        assertEquals("Test Task", taskDTO.getName());
        assertEquals("Test Description", taskDTO.getDescription());
        assertEquals("pending", taskDTO.getStatus());
    }

    @Test
    @DisplayName("Test get task by Id with an incorrect project throws exception")
    public void testGetTaskByIdWithIncorrectProject() {
        Project projectTest = Project.builder().build();
        task.setProject(projectTest);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(ForbbidenException.class, ()-> taskService.getTaskById(projectDTO, 1L));
    }

    @Test
    @DisplayName("Test update task")
    public void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDTO taskDTOUpdated = TaskDTO.builder()
                .name("Test Task updated")
                .description("Test Description updated")
                .status("onHold")
                .build();

        assertDoesNotThrow(()-> taskService.updateTask(projectDTO, 1L, taskDTOUpdated));
    }

    @Test
    @DisplayName("Test update task status")
    public void testUpdateTaskStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        String status = "onHold"; //Allowed status are pending, onHold, inProgress, underReview & completed.
        TaskDTO taskDTO = TaskDTO.builder().status(status).build();

        assertDoesNotThrow(()-> taskService.updateTaskStatus(projectDTO, taskDTO, 1L));
    }

    @Test
    @DisplayName("Test update task status invalid throws exception")
    public void testUpdateTaskStatusInvalid() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        String status = "new"; //Allowed status are pending, onHold, inProgress, underReview & completed.
        TaskDTO taskDTO = TaskDTO.builder().status(status).build();

        assertThrows(BadRequestException.class, ()-> taskService.updateTaskStatus(projectDTO, taskDTO, 1L));
    }

    @Test
    @DisplayName("Test delete task")
    public void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertDoesNotThrow(()-> taskService.deleteTask(projectDTO, 1L));
        verify(taskRepository, times(1)).delete(task);
    }
}
