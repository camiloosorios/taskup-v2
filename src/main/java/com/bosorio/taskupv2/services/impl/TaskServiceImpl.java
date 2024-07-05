package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.Exceptions.ForbbidenException;
import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.entites.Task;
import com.bosorio.taskupv2.repositories.TaskRepository;
import com.bosorio.taskupv2.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.bosorio.taskupv2.utils.ModelConverter.dtoToTask;
import static com.bosorio.taskupv2.utils.ModelConverter.taskToDTO;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void createTask(TaskDTO taskDTO, ProjectDTO projectDTO) {
        Project project = Project.builder()
                .id(projectDTO.getId())
                .build();

        Task task = dtoToTask(taskDTO);
        task.setProject(project);
        task.setStatus("pending");

        try {
            taskRepository.save(task);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public List<TaskDTO> getProjectTasks(ProjectDTO projectDTO) {
        List<Task> tasks = taskRepository.findByProject_Id(projectDTO.getId());
        List<TaskDTO> tasksDTO = new ArrayList<>();
        tasks.forEach(task -> tasksDTO.add(taskToDTO(task)));

        return tasksDTO;
    }

    @Override
    public TaskDTO getTaskById(ProjectDTO projectDTO, Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        if (projectDTO.getId() != task.getProject().getId()) {
            throw new ForbbidenException("Invalid Action");
        }
        return taskToDTO(task);
    }

    @Override
    public void updateTask(ProjectDTO projectDTO, Long id, TaskDTO taskDTO) {
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        if (projectDTO.getId() != taskToUpdate.getProject().getId()) {
            throw new ForbbidenException("Invalid Action");
        }
        Task taskUpdated = dtoToTask(taskDTO);
        taskUpdated.setId(id);
        taskUpdated.setProject(taskToUpdate.getProject());
        taskUpdated.setUpdatedAt(LocalDateTime.now());
        try {
            taskRepository.save(taskUpdated);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void updateTaskStatus(ProjectDTO projectDTO, String status, Long id) {

    }

    @Override
    public void deleteTask(ProjectDTO projectDTO, Long id) {

    }
}
