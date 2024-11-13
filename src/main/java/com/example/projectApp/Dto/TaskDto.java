package com.example.projectApp.Dto;
import com.example.projectApp.model.Task;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private boolean completed ;
    private ProjectDto project;
    private UserDto assigned_to;

    public TaskDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.completed = task.isCompleted();
        this.assigned_to = task.getUser() != null ?
                new UserDto(task.getUser(), false) :
                null;
        this.project = task.getProject()!= null ?
                new ProjectDto(task.getProject(), false) :
                null;
    }
}