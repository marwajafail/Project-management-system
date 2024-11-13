package com.example.projectApp.model;

import com.example.projectApp.Dto.ProjectDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "startDate",nullable = false)
    private LocalDateTime startDate;

    @Column(name = "endDate",nullable = false)
    private LocalDateTime endDate;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"typeId\"")
    @ToString.Exclude
    @JsonBackReference
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Task> tasks;// Tasks associated with this project

    public Project(ProjectDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.description =dto.getDescription();
        this.startDate=dto.getStartDate();
        this.endDate=dto.getEndDate();
        this.type = dto.getType() != null ?
                new Type(dto.getType()) :
                null;
        this.user = dto.getUser() != null ?
                new User(dto.getUser()) :
                null;
        this.tasks = dto.getTasks() != null ?
                dto.getTasks().stream().map(Task::new).toList() :
                null;
    }



}
