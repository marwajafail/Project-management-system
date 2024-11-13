package com.example.projectApp.repository;

import com.example.projectApp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndProjectId(Long id, Long projectId);

    List<Task> findAllByProjectId(Long projectId);
    List<Task> findByUserId(Long userId);

}
