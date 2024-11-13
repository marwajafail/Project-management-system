package com.example.projectApp.controller;
import com.example.projectApp.service.UserService;
import com.example.projectApp.storage.StorageFileNotFoundException;
import com.example.projectApp.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/files")

public class FileUploadController {

    private final StorageService storageService;
    private final UserService userService;

    @Autowired
    public FileUploadController(StorageService storageService, UserService userService) {
        this.storageService = storageService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<?> listUploadedFiles() {
        try {
            List<String> files = storageService.loadAll().map(
                    path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "serveFile", path.getFileName().toString()).build().toUri().toString())
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(files);
        } catch (Exception e) {
            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);

            if (file == null)
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (Exception e) {
            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> handleFileUpload(@RequestParam("files") MultipartFile[] files) {
        try {
            // Check if at least one file was uploaded
            if (files.length < 1) {
                return new ResponseEntity<>("You need to upload at least 1 file.", HttpStatus.BAD_REQUEST);
            }

            // Store each file
            for (MultipartFile file : files) {
                storageService.store(file);
            }

            // Create a message with the names of the uploaded files
            StringBuilder message = new StringBuilder("Successfully uploaded: ");
            for (int i = 0; i < files.length; i++) {
                if (i > 0) message.append(", ");
                message.append(files[i].getOriginalFilename());
            }
            message.append("!");

            return ResponseEntity.status(HttpStatus.OK).body(message.toString());
        } catch (Exception e) {
            // Print error to the console and return a bad request response
            System.out.println("An error occurred: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}