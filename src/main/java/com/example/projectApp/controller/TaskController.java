package com.example.projectApp.controller;


import com.example.projectApp.Dao.GenericDao;
import com.example.projectApp.Dto.TaskDto;
import com.example.projectApp.service.TaskService;
import com.example.projectApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")

public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // Get all tasks record
    @GetMapping("/project/{projectId}")
    public ResponseEntity<GenericDao<List<TaskDto>>> getAllTasksByProjectId(@PathVariable(value = "projectId") Long projectId) {
        try {
            GenericDao<List<TaskDto>> genericDao = taskService.getByProjectId(projectId);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.OK) :
                    new ResponseEntity<>(genericDao, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To read an tasks record by task Id and project Id
    @GetMapping("/{taskId}/project/{projectId}")
    public ResponseEntity<GenericDao<TaskDto>> getTasksByTaskIdAndProjectId(@PathVariable(value = "taskId") Long taskId, @PathVariable(value = "projectId") Long projectId) {
        try {
            GenericDao<TaskDto> genericDao = taskService.getByTaskIdAndProjectId(taskId, projectId);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.OK) :
                    new ResponseEntity<>(genericDao, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To create a new task record
    @PostMapping
    public ResponseEntity<GenericDao<TaskDto>> createTask(@RequestBody TaskDto taskDto) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {

                GenericDao<TaskDto> genericDao = taskService.createTask(taskDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To update task
    @PutMapping
    public ResponseEntity<GenericDao<TaskDto>> editTask(@RequestBody  TaskDto taskDto) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {

                GenericDao<TaskDto> genericDao = taskService.editTask(taskDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To delete task by taskId
    @DeleteMapping("/{taskId}")
    public ResponseEntity<GenericDao<Boolean>> deleteTask(@PathVariable(value = "taskId") Long taskId) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {
                // Call service method to delete the task by taskId
                GenericDao<Boolean> genericDao = taskService.deleteTask(taskId);

                // Check for errors and return appropriate response
                return genericDao.getErrors().isEmpty() ?
                        new ResponseEntity<>(genericDao, HttpStatus.CREATED) :  // Success
                        new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST); // Failure
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Unauthorized access
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return bad request on exception
        }
    }


    @PutMapping("/{taskId}/status")
    public ResponseEntity<GenericDao<TaskDto>> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody TaskDto taskDto) {
        try {

            GenericDao<TaskDto> genericDao = taskService.updateTaskStatus(taskId, taskDto.isCompleted());

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.OK) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/assigned")
    public ResponseEntity<GenericDao<List<TaskDto>>> getTasksAssignedToCurrentUser() {
        GenericDao<List<TaskDto>> response = taskService.getTasksAssignedToCurrentUser();
        return ResponseEntity.ok(response);
    }



}