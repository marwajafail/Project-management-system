package com.example.projectApp.repository;

import com.example.projectApp.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    Optional<Type> findByIdAndUserIdOrUserId(Long id, Long userId, Long adminId);
    Optional<Type> findByName(String name);

    Optional<Type> findByIdAndUserId(Long id, Long userId);

    Optional<Type> findByNameAndUserId(String typeName, Long userId);

    Optional<Type> findByNameAndUserIdOrUserId(String typeName, Long userId, Long adminId);

    List<Type> findAllByUserIdOrUserId(Long userId, Long adminId);



}


