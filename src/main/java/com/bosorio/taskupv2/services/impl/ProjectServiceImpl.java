package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.entites.Task;
import com.bosorio.taskupv2.repositories.ProjectRepository;
import com.bosorio.taskupv2.services.ProjectService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bosorio.taskupv2.utils.ModelConverter.*;

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
        projects.forEach(project -> projectsDTO.add(projectToDTO(project)));

        return projectsDTO;
    }

    @Override
    public void createProject(ProjectDTO projectDTO) {
        Project project = dtoToProject(projectDTO);
        try {
            projectRepository.save(project);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        return projectToDTO(project);
    }

    @Override
    public void updateProject(Long id, ProjectDTO projectDTO) {
        ProjectDTO projectToUpdate = getProjectById(id);
        List<Task> tasks = new ArrayList<>();

        projectToUpdate.getTasks().forEach(taskDTO -> tasks.add(dtoToTask(taskDTO)));
        Project projectUpdated = dtoToProject(projectDTO);
        tasks.forEach(task -> task.setProject(projectUpdated));
        projectUpdated.setId(id);
        projectUpdated.setTasks(tasks);
        try {
            projectRepository.save(projectUpdated);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void deleteProject(Long id) {
        ProjectDTO projectToDelete = getProjectById(id);
        Project project = dtoToProject(projectToDelete);
        project.setId(id);
        try {
            projectRepository.delete(project);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
