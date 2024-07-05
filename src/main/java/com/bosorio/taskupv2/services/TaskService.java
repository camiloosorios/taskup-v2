package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;

import java.util.List;

public interface TaskService {

    void createTask(TaskDTO taskDTO, ProjectDTO projectDTO);

    List<TaskDTO> getProjectTasks(ProjectDTO projectDTO);

    TaskDTO getTaskById(ProjectDTO projectDTO, Long id);

    void updateTask(ProjectDTO projectDTO, Long id, TaskDTO taskDTO);

    void updateTaskStatus(ProjectDTO projectDTO, String status, Long id);

    void deleteTask(ProjectDTO projectDTO, Long id);

}
