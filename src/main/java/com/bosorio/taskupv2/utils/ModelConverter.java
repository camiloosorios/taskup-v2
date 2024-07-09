package com.bosorio.taskupv2.utils;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.Exceptions.BadRequestException;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.entites.Task;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {
    public static TaskDTO taskToDTO(Task task) {
        validateStatus(task.getStatus());
        return TaskDTO.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    public static Task dtoToTask(TaskDTO taskDTO) {
        validateStatus(taskDTO.getStatus());
        return Task.builder()
                .id(taskDTO.getId())
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus())
                .createdAt(taskDTO.getCreatedAt())
                .updatedAt(taskDTO.getUpdatedAt())
                .build();
    }

    public static ProjectDTO projectToDTO(Project project) {
        List<TaskDTO> tasksDTO = new ArrayList<>();
        project.getTasks().forEach(task -> {
            tasksDTO.add(taskToDTO(task));
        });
        return ProjectDTO.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .clientName(project.getClientName())
                .tasks(tasksDTO)
                .build();
    }

    public static Project dtoToProject(ProjectDTO projectDTO) {
        List<Task> tasks = new ArrayList<>();
        if (projectDTO.getTasks() != null) {
            projectDTO.getTasks().forEach(taskDTO -> {
                tasks.add(dtoToTask(taskDTO));
            });
        }
        return Project.builder()
                .id(projectDTO.getId())
                .projectName(projectDTO.getProjectName())
                .description(projectDTO.getDescription())
                .clientName(projectDTO.getClientName())
                .tasks(tasks)
                .build();
    }

    public static void validateStatus(String status) {
        if (!status.equals("pending") &&
                !status.equals("onHold") &&
                !status.equals("inProgress") &&
                !status.equals("underReview") &&
                !status.equals("completed")) {
            throw new BadRequestException("Status not allowed " + status);
        }
    }
}
