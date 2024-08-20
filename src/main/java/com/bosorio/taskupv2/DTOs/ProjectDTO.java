package com.bosorio.taskupv2.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Project Name must not be blank")
    private String projectName;

    @NotBlank(message = "Client Name must not be blank")
    private String clientName;

    @NotBlank(message = "Description must not be blank")
    private String description;

    private List<TaskDTO> tasks;

    private UserDTO manager;

    private Set<UserDTO> members;

}
