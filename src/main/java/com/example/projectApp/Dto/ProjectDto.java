package com.example.projectApp.Dto;
import com.example.projectApp.model.Project;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TypeDto type;
    private UserDto user;
    private List<TaskDto> tasks;

    public ProjectDto(Project project, boolean details) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.type = project.getType() != null ?
                new TypeDto(project.getType(), false) :
                null;
        this.user = project.getUser() != null ?
                new UserDto(project.getUser(), false) :
                null;
        if (details) {
            this.tasks = project.getTasks() != null ?
                    project.getTasks().stream().peek(task -> task.setProject(null)).map(TaskDto::new).toList() :
                    new ArrayList<>();
        }
    }
}