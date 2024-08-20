package com.bosorio.taskupv2.utils;

import com.bosorio.taskupv2.DTOs.NoteDTO;
import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.DTOs.TaskDTO;
import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.Exceptions.BadRequestException;
import com.bosorio.taskupv2.entites.Note;
import com.bosorio.taskupv2.entites.Project;
import com.bosorio.taskupv2.entites.Task;
import com.bosorio.taskupv2.entites.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Set<UserDTO> membersDTO = new HashSet<>();
        if (project.getTasks() != null) {
            project.getTasks().forEach(task -> tasksDTO.add(taskToDTO(task)));
        }
        if (project.getMembers() != null) {
            project.getMembers().forEach(member -> membersDTO.add(userToDTO(member)));
        }
        return ProjectDTO.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .clientName(project.getClientName())
                .tasks(tasksDTO)
                .manager(userToDTO(project.getManager()))
                .members(membersDTO)
                .build();
    }

    public static Project dtoToProject(ProjectDTO projectDTO) {
        List<Task> tasks = new ArrayList<>();
        Set<User> members = new HashSet<>();
        if (projectDTO.getTasks() != null) {
            projectDTO.getTasks().forEach(taskDTO -> tasks.add(dtoToTask(taskDTO)));
        }
        if (projectDTO.getMembers() != null) {
            projectDTO.getMembers().forEach(membersDto -> members.add(dtoToUser(membersDto)));
        }
        return Project.builder()
                .id(projectDTO.getId())
                .projectName(projectDTO.getProjectName())
                .description(projectDTO.getDescription())
                .clientName(projectDTO.getClientName())
                .tasks(tasks)
                .manager(dtoToUser(projectDTO.getManager()))
                .members(members)
                .build();
    }

    public static UserDTO userToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .confirmed(user.getConfirmed())
                .build();
    }

    public static User dtoToUser(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .confirmed(userDTO.getConfirmed())
                .build();
    }

    public static NoteDTO noteToDTO(Note note) {
        return NoteDTO.builder()
                .id(note.getId())
                .content(note.getContent())
                .createdBy(userToDTO(note.getCreatedBy()))
                .task(taskToDTO(note.getTask()))
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    public static Note dtoToNote(NoteDTO noteDTO) {
        return Note.builder()
                .id(noteDTO.getId())
                .content(noteDTO.getContent())
                .createdBy(dtoToUser(noteDTO.getCreatedBy()))
                .task(dtoToTask(noteDTO.getTask()))
                .createdAt(noteDTO.getCreatedAt())
                .updatedAt(noteDTO.getUpdatedAt())
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
