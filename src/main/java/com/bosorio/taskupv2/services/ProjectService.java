package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.ProjectDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectDTO> getAllProjects();

    void createProject(ProjectDTO projectDTO);

    ProjectDTO getProjectById(Long id);

    void updateProject(Long id, ProjectDTO projectDTO);

    void deleteProject(Long id);

}
