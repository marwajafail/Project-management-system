package com.example.projectApp.model;

import com.example.projectApp.Dto.TypeDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "type")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeId", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name; // Type name (Internal, External)

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Project> projects;



    public Type(TypeDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.user = dto.getUser() != null ?
                new User(dto.getUser()) :
                null;
        this.projects = dto.getProjects()!= null ?
                dto.getProjects().stream().map(Project::new).toList() :
                null;
    }

}