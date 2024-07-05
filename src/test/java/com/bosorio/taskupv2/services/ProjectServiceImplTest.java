package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.repositories.ProjectRepository;
import com.bosorio.taskupv2.services.impl.ProjectServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private ProjectDTO projectDTO;
    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectDTO = ProjectDTO.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .build();

        project = Project.builder()
                .projectName("Test Project")
                .clientName("Test Client")
                .description("Test Description")
                .tasks(Arrays.asList())
                .build();
    }

    @Test
    @DisplayName("Test create project")
    void testCreateProject() {
        assertDoesNotThrow(() -> projectService.createProject(projectDTO));
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Test get all projects")
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project, project));

        List<ProjectDTO> projectsDTO = projectService.getAllProjects();

        verify(projectRepository, times(1)).findAll();
        assertEquals(2, projectsDTO.size());
    }

    @Test
    @DisplayName("Test get project by Id")
    void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectDTO projectDTO = projectService.getProjectById(1L);

        verify(projectRepository, times(1)).findById(1L);
        assertEquals("Test Project", projectDTO.getProjectName());
    }

    @Test
    @DisplayName("Test get project with no existent Id throws exception")
    void testGetProjectWithNoExistentId() {
        assertThrows(NotFoundException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    @DisplayName("Test update project")
    void testUpdateProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectDTO updatedProject = ProjectDTO.builder()
                .projectName("Test Project Updated")
                .clientName("Test Client Updated")
                .description("Test Description Updated")
                .build();

        assertDoesNotThrow(() -> projectService.updateProject(1L, updatedProject));
    }

    @Test
    @DisplayName("Test delete project")
    void testDeleteProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> projectService.deleteProject(1L));
        verify(projectRepository, times(1)).findById(1L);
    }
}
