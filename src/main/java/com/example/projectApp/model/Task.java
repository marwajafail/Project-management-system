package com.example.projectApp.model;

import com.example.projectApp.Dto.TaskDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "isCompleted")
    private boolean completed; // Indicates if the task is completed

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"projectId\"")
    @ToString.Exclude
    private Project project;// The project to which the task belongs


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    @JsonBackReference
    @ToString.Exclude
    private User user;// User assigned to the task

    public Task(TaskDto dto) {
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.completed = dto.isCompleted();
        this.user = dto.getAssigned_to() != null ?
                new User(dto.getAssigned_to()) :
                null;
        this.project = dto.getProject() != null ?
                new Project(dto.getProject()) :
                null;
    }


}
