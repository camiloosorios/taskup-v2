package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.UserDTO;

import java.util.List;
import java.util.Set;

public interface ProjectService {

    List<ProjectDTO> getAllProjects();

    void createProject(ProjectDTO projectDTO);

    ProjectDTO getProjectById(Long id);

    void updateProject(Long id, ProjectDTO projectDTO);

    void deleteProject(Long id);

    void addMember(Long userId, ProjectDTO projectDTO);

    void removeMember(Long userId, ProjectDTO projectDTO);

    Set<UserDTO> getProjectMembers(ProjectDTO projectDTO);

}
