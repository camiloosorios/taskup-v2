package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.repositories.ProjectRepository;
import com.bosorio.taskupv2.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> projectsDTO = new ArrayList<>();
        projects.forEach(project -> {
            projectsDTO.add(ProjectDTO.builder()
                    .id(project.getId())
                    .projectName(project.getProjectName())
                    .clientName(project.getClientName())
                    .description(project.getDescription())
                    .build());
        });

        return projectsDTO;
    }

    @Override
    public void createProject(ProjectDTO projectDTO) {
        Project project = Project.builder()
                .projectName(projectDTO.getProjectName())
                .clientName(projectDTO.getClientName())
                .description(projectDTO.getDescription())
                .build();
        try {
            projectRepository.save(project);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        return ProjectDTO.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .clientName(project.getClientName())
                .description(project.getDescription())
                .build();
    }

    @Override
    public void updateProject(Long id, ProjectDTO projectDTO) {
        ProjectDTO projectToUpdate = getProjectById(id);
        Project projectUpdated = Project.builder()
                .id(id)
                .projectName(projectDTO.getProjectName())
                .clientName(projectDTO.getClientName())
                .description(projectDTO.getDescription())
                .build();

        try {
            projectRepository.save(projectUpdated);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void deleteProject(Long id) {
        ProjectDTO projectToDelete = getProjectById(id);
        Project project = Project.builder()
                .id(id)
                .projectName(projectToDelete.getProjectName())
                .clientName(projectToDelete.getClientName())
                .description(projectToDelete.getDescription())
                .build();

        try {
            projectRepository.delete(project);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
