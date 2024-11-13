package com.example.projectApp.repository;

import com.example.projectApp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndUserId(Long id, Long userId);

    Optional<Project> findByNameAndUserId(String name, Long userId);

    List<Project> findAllByUserId(Long userId);
}
