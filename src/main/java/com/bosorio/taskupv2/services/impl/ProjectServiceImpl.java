package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.Exceptions.BadRequestException;
import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.entites.Task;
import com.bosorio.taskupv2.entites.User;
import com.bosorio.taskupv2.repositories.ProjectRepository;
import com.bosorio.taskupv2.services.ProjectService;
import com.bosorio.taskupv2.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.bosorio.taskupv2.utils.ModelConverter.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final UserService userService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> projectsDTO = new ArrayList<>();
        projects.forEach(project -> projectsDTO.add(projectToDTO(project)));

        return projectsDTO;
    }

    @Override
    @Transactional
    public void createProject(ProjectDTO projectDTO) {
        Long userId = Long.valueOf(getAuthenticatedUser());
        UserDTO userDTO = userService.getUser(userId);
        User user = dtoToUser(userDTO);
        Project project = Project.builder()
                .projectName(projectDTO.getProjectName())
                .clientName(projectDTO.getClientName())
                .description(projectDTO.getDescription())
                .manager(user)
                .build();
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
    @Transactional
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
    @Transactional
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

    @Override
    @Transactional
    public void addMember(Long userId, ProjectDTO projectDTO) {
        User user = dtoToUser(userService.getUser(userId));
        Project project = projectRepository.findById(projectDTO.getId())
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getMembers() == null) {
            project.setMembers(new HashSet<>());
        }
        project.getMembers().add(user);
        try {
            projectRepository.save(project);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void removeMember(Long userId, ProjectDTO projectDTO) {
        User user = dtoToUser(userService.getUser(userId));
        Project project = projectRepository.findById(projectDTO.getId())
                .orElseThrow(() -> new NotFoundException("Project not found"));
        if (project.getMembers() == null || !project.getMembers().contains(user)) {
            throw new BadRequestException("User is not a member of project");
        }
        project.getMembers().remove(user);
        try {
            projectRepository.save(project);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public Set<UserDTO> getProjectMembers(ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectDTO.getId())
                .orElseThrow(() -> new NotFoundException("Project not found"));
        Set<UserDTO> members = new HashSet<>();
        project.getMembers().forEach(member -> members.add(userToDTO(member)));

        return members;
    }

    private String getAuthenticatedUser() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
