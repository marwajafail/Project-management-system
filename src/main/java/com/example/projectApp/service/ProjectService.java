package com.example.projectApp.service;

import com.example.projectApp.Dao.GenericDao;
import com.example.projectApp.Dto.ProjectDto;
import com.example.projectApp.model.Project;
import com.example.projectApp.model.Type;
import com.example.projectApp.repository.ProjectRepository;
import com.example.projectApp.repository.TypeRepository;
import com.example.projectApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;


    @Autowired
    public ProjectService(ProjectRepository projectRepository, TypeRepository typeRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
    }

    public ProjectDto getById(Long projectId, Boolean details) {
        return projectRepository.findByIdAndUserId(projectId, UserService.getCurrentLoggedInUser().getId()).map(project -> new ProjectDto(project, details)).orElse(null);
    }

    public List<ProjectDto> getAll(Boolean details) {
        return projectRepository.findAllByUserId(UserService.getCurrentLoggedInUser().getId()).stream().map(project -> new ProjectDto(project, details)).toList();
    }

    public GenericDao<ProjectDto> createProject(ProjectDto dto) {
        GenericDao<ProjectDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();

        if (dto.getName() == null) {
            errors.add("Project name cannot be empty");
        }

        if (dto.getDescription() == null) {
            errors.add("Description cannot be empty");
        }


        if (dto.getStartDate() == null) {
            errors.add("StartDate cannot be empty");
        }

        if (dto.getEndDate() == null) {
            errors.add("EndDate cannot be empty");
        }


        Optional<Type> type = Optional.empty();
        if (dto.getType().getId() == null) {
            errors.add("Type ID cannot be empty");
        } else {
            type = typeRepository.findByIdAndUserId(dto.getType().getId(), UserService.getCurrentLoggedInUser().getId());
            if (type.isEmpty()) {
                errors.add("Type does not exists");
            }
        }

        if (errors.isEmpty()) {
            Long uid = UserService.getCurrentLoggedInUser().getId();
            Optional<Project> retrievedProject = projectRepository.findByNameAndUserId(dto.getName(), uid);
            if (retrievedProject.isEmpty() || (retrievedProject.get().getName().equals(dto.getName()) && retrievedProject.get().getDescription() != dto.getDescription()&& retrievedProject.get().getStartDate() != dto.getStartDate()&& retrievedProject.get().getEndDate() != dto.getEndDate())) {
                Project project = new Project(dto);
                project.setType(type.get());
                project.setUser(userRepository.findById(uid).get());
                Project savedProject = projectRepository.save(project);
                returnDao.setObject(new ProjectDto(savedProject, false));
            } else {
                errors.add("Type already exists");
            }
        }
        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }
        return returnDao;
    }


    public GenericDao<ProjectDto> editProject(ProjectDto dto) {
        GenericDao<ProjectDto> returnDao = new GenericDao<>();

        List<String> errors = new ArrayList<>();

        if (dto.getId() == null) {
            errors.add("Project ID cannot be empty");
        }

        if (dto.getName() == null) {
            errors.add("Project name cannot be empty");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            errors.add("Project name cannot be empty");
        }

        if (dto.getDescription() == null) {
            errors.add("Description cannot be empty");
        }

        if (dto.getStartDate() == null) {
            errors.add("StartDate cannot be empty");
        }

        if (dto.getEndDate() == null) {
            errors.add("EndDate cannot be empty");
        }

        if (errors.isEmpty()) {
            Optional<Project> retrievedProject = projectRepository.findByIdAndUserId(dto.getId(), UserService.getCurrentLoggedInUser().getId());

            if (retrievedProject.isPresent()) {
                if (retrievedProject.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                    retrievedProject.get().setName(dto.getName());
                    retrievedProject.get().setDescription(dto.getDescription());
                    retrievedProject.get().setStartDate(dto.getStartDate());
                    retrievedProject.get().setEndDate(dto.getEndDate());


                    Project savedProject = projectRepository.save(retrievedProject.get());

                    returnDao.setObject(new ProjectDto(savedProject, false));
                } else {
                    errors.add("Cannot edit project of another user");
                }
            } else {
                errors.add("Project does not exist");
            }
        }

        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }

    public GenericDao<Boolean> deleteProject(Long projectId) {
        Optional<Project> retrievedProject = projectRepository.findByIdAndUserId(projectId, UserService.getCurrentLoggedInUser().getId());
        List<String> errors = new ArrayList<>();
        if (retrievedProject.isPresent()) {
            if (retrievedProject.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                projectRepository.deleteById(projectId);
            } else {
                errors.add("Cannot delete project of another user");
            }
            return errors.isEmpty() ?
                    new GenericDao<>(true, errors) :
                    new GenericDao<>(false, errors);
        } else {
            errors.add("Project does not exist");
            return new GenericDao<>(false, errors);
        }
    }

}