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


}