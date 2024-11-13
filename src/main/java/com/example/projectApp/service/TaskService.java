package com.example.projectApp.service;

import com.example.projectApp.Dao.GenericDao;
import com.example.projectApp.Dto.ProjectDto;
import com.example.projectApp.Dto.TaskDto;
import com.example.projectApp.model.Project;
import com.example.projectApp.model.Task;
import com.example.projectApp.model.User;
import com.example.projectApp.repository.ProjectRepository;
import com.example.projectApp.repository.TaskRepository;
import com.example.projectApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository , UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }


    public GenericDao<TaskDto> getByTaskIdAndProjectId(Long taskId, Long projectId) {
        GenericDao<TaskDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();
        Optional<Project> project = projectRepository.findByIdAndUserId(projectId, UserService.getCurrentLoggedInUser().getId());
        if (project.isEmpty()) {
            errors.add("Project not found");
        }

        if (errors.isEmpty()) {
            Optional<Task> retrievedTask = taskRepository.findByIdAndProjectId(taskId, projectId);
            if (retrievedTask.isEmpty()) {
                errors.add("Task not found");
            } else {
                returnDao.setObject(new TaskDto(retrievedTask.get()));
            }
        }
        returnDao.setErrors(errors);
        return returnDao;
    }

    public GenericDao<List<TaskDto>> getByProjectId(Long projectId) {
        GenericDao<List<TaskDto>> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();
        Optional<Project> project = projectRepository.findByIdAndUserId(projectId, UserService.getCurrentLoggedInUser().getId());

        if (project.isEmpty()) {
            errors.add("Project not found");
        }

        if (errors.isEmpty()) {
            List<Task> retrievedTask = taskRepository.findAllByProjectId(projectId);
            returnDao.setObject(retrievedTask.stream().map(TaskDto::new).toList());
        }
        return returnDao;
    }

    public GenericDao<TaskDto> createTask(TaskDto dto) {
        GenericDao<TaskDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            errors.add("Task title cannot be empty");
        }
        if (dto.getDescription() == null) {
            errors.add("Description cannot be empty");
        }

        Optional<Project> project = Optional.empty();
        if (dto.getProject().getId() == null) {
            errors.add("Project ID cannot be empty");
        } else {
            project = projectRepository.findByIdAndUserId(dto.getProject().getId(), UserService.getCurrentLoggedInUser().getId());
            if (project.isEmpty()) {
                errors.add("Project does not exists");
            }
        }

        // Validate assigned user
        Optional<User> user = Optional.empty();
        if (dto.getAssigned_to() == null || dto.getAssigned_to().getId() == null) {
            errors.add("Assigned User ID cannot be empty");
        } else {
            user = userRepository.findById(dto.getAssigned_to().getId());
            if (user.isEmpty()) {
                errors.add("Assigned User does not exist");
            }
        }

        // Save the task if no errors
        if (errors.isEmpty()) {
            dto.setProject(new ProjectDto(project.get(), false));
            Task task = new Task(dto);
            task.setUser(user.get());  // Assign user to task

            Task savedTask = taskRepository.save(task);
            returnDao.setObject(new TaskDto(savedTask));
        } else {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }

    public GenericDao<TaskDto> editTask(TaskDto dto) {
        GenericDao<TaskDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();

        // Validate Task ID
        if (dto.getId() == null) {
            errors.add("Task ID cannot be empty");
        }

        // Validate Title
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            errors.add("Task title cannot be empty");
        }

        // Validate Description
        if (dto.getDescription() == null) {
            errors.add("Description cannot be empty");
        }

        // Validate Completed Status
        if (Boolean.FALSE.equals(dto.isCompleted())) {
            errors.add("Completed cannot be empty");
        }

        // Check if errors list is empty before proceeding
        if (errors.isEmpty()) {
            Optional<Task> retrievedTask = taskRepository.findById(dto.getId());

            if (retrievedTask.isPresent()) {
                Task task = retrievedTask.get();

                // Check if current user owns the task's project
                if (task.getProject().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                    task.setTitle(dto.getTitle());
                    task.setDescription(dto.getDescription());
                    task.setCompleted(dto.isCompleted());

                    // Validate and assign new user if provided
                    if (dto.getAssigned_to() != null && dto.getAssigned_to().getId() != null) {
                        Optional<User> user = userRepository.findById(dto.getAssigned_to().getId());
                        if (user.isPresent()) {
                            task.setUser(user.get());
                        } else {
                            errors.add("Assigned User does not exist");
                        }
                    }

                    // Save updated task if no user-related errors
                    if (errors.isEmpty()) {
                        Task savedTask = taskRepository.save(task);
                        returnDao.setObject(new TaskDto(savedTask));
                    }

                } else {
                    errors.add("Cannot edit task of another user");
                }
            } else {
                errors.add("Task does not exist");
            }
        }

        // Return errors if any
        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }

    public GenericDao<Boolean> deleteTask(Long taskId) {
        GenericDao<Boolean> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();

        // Find the task by taskId
        Optional<Task> retrievedTask = taskRepository.findById(taskId);
        if (!retrievedTask.isPresent()) {
            errors.add("Task does not exist");
            returnDao.setObject(false);  // Task not found
            returnDao.setErrors(errors);
            return returnDao;
        }

        // Authorization check: Ensure the task belongs to the current user
        if (!retrievedTask.get().getProject().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
            errors.add("Cannot delete task of another user");
            returnDao.setObject(false);  // Unauthorized action
            returnDao.setErrors(errors);
            return returnDao;
        }

        // Delete the task if all checks pass
        taskRepository.deleteById(taskId);
        returnDao.setObject(true);  // Task successfully deleted
        returnDao.setErrors(errors);

        return returnDao;
    }




    public GenericDao<TaskDto> updateTaskStatus(Long taskId, Boolean completed) {
        GenericDao<TaskDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();


        Optional<Task> retrievedTask = taskRepository.findById(taskId);
        if (retrievedTask.isEmpty()) {
            errors.add("Task does not exist");
        } else {
            Task task = retrievedTask.get();

            if (task.getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                task.setCompleted(completed);
                Task updatedTask = taskRepository.save(task);
                returnDao.setObject(new TaskDto(updatedTask));
            } else {
                errors.add("Cannot update status of task assigned to another user");
            }
        }

        returnDao.setErrors(errors);
        return returnDao;
    }


    public GenericDao<List<TaskDto>> getTasksAssignedToCurrentUser() {
        GenericDao<List<TaskDto>> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();
        List<TaskDto> taskDto = new ArrayList<>();

        // Get the current logged-in user
        User currentUser = UserService.getCurrentLoggedInUser();

        if (currentUser == null) {
            errors.add("User not logged in");
        } else {
            // Retrieve tasks assigned to the current user
            List<Task> tasks = taskRepository.findByUserId(currentUser.getId());

            // Convert Task entities to TaskDto
            taskDto = tasks.stream()
                    .map(TaskDto::new)
                    .collect(Collectors.toList());
        }

        returnDao.setObject(taskDto);
        returnDao.setErrors(errors);
        return returnDao;
    }


}