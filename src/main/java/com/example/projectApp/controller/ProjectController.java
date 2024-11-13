package com.example.projectApp.controller;
import com.example.projectApp.Dao.GenericDao;
import com.example.projectApp.Dto.ProjectDto;
import com.example.projectApp.service.ProjectService;
import com.example.projectApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;


    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    // Get all Project record
    @GetMapping
    public ResponseEntity<GenericDao<List<ProjectDto>>> getAllProjects(@RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {

                List<ProjectDto> projectDto = projectService.getAll(details);

            return !projectDto.isEmpty() ?
                    new ResponseEntity<>(new GenericDao<>(projectDto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("No projects found")), HttpStatus.NOT_FOUND);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To read all projects record by type Id
    @GetMapping("/{projectId}")
    public ResponseEntity<GenericDao<ProjectDto>> getProjectById(@PathVariable(value = "projectId") Long projectId, @RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {

                ProjectDto projectDto = projectService.getById(projectId, details);

            return projectDto != null ?
                    new ResponseEntity<>(new GenericDao<>(projectDto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("Project with the id " + projectId + " was not found")), HttpStatus.NOT_FOUND);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To create a new project record
    @PostMapping
    public ResponseEntity<GenericDao<ProjectDto>> createProject(@RequestBody ProjectDto projectDto) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {
            GenericDao<ProjectDto> genericDao = projectService.createProject(projectDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To edit project
    @PutMapping
    public ResponseEntity<GenericDao<ProjectDto>> editProject(@RequestBody ProjectDto projectDto) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {
            GenericDao<ProjectDto> genericDao = projectService.editProject(projectDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }}catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // To delete project
    @DeleteMapping("/{projectId}")
    public ResponseEntity<GenericDao<Boolean>> deleteProject(@PathVariable(value = "projectId") Long projectId) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {
            GenericDao<Boolean> genericDao = projectService.deleteProject(projectId);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}