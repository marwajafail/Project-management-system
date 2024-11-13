package com.example.projectApp.Dto;
import com.example.projectApp.Dto.UserDto;
import com.example.projectApp.model.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private UserDto user;
    private List<ProjectDto> projects;

    public TypeDto(Type type, boolean details) {
        this.id = type.getId();
        this.name = type.getName();
        this.description = type.getDescription();
        this.user = type.getUser() != null ?
                new UserDto(type.getUser(), false) :
                null;
        if (details) {
            this.projects = type.getProjects() != null ?
                    type.getProjects().stream().peek(project -> project.setType(null)).map(budget -> new ProjectDto(budget, false)).toList() :
                    new ArrayList<>();
        }
    }
}