package com.example.projectApp.controller;

import com.example.projectApp.Dao.GenericDao;
import com.example.projectApp.Dto.TypeDto;
import com.example.projectApp.service.TypeService;
import com.example.projectApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class TypeController {

    private final TypeService typeService;



    @Autowired
    public TypeController(TypeService typeService , UserService userService) {
        this.typeService = typeService;

    }
    // To read all type records
    @GetMapping
    public ResponseEntity<GenericDao<List<TypeDto>>> getAllTypes(@RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {

                List<TypeDto> dto = typeService.getAll(details);
            return !dto.isEmpty() ?
                    new ResponseEntity<>(new GenericDao<>(dto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("No types found")), HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //To create a new type record
    @PostMapping
    public ResponseEntity<GenericDao<TypeDto>> createType(@RequestBody TypeDto typeDto) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {
            GenericDao<TypeDto> genericDao = typeService.createType(typeDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To read a type record
    @GetMapping("/{typeId}")
    public ResponseEntity<GenericDao<TypeDto>> getTypeById(@PathVariable(value = "typeId") Long typeId, @RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {

                TypeDto typeDto = typeService.getById(typeId, details);

            return typeDto != null ?
                    new ResponseEntity<>(new GenericDao<>(typeDto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("Type with the id " + typeId + " was not found")), HttpStatus.NOT_FOUND);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // To read a type record by name
    @GetMapping("/name/{typeName}")
    public ResponseEntity<GenericDao<TypeDto>> getTypeByName(@PathVariable(value = "typeName") String typeName, @RequestParam(value = "details", defaultValue = "false", required = false) boolean details) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {

                TypeDto typeDto = typeService.getByName(typeName, details);

            return typeDto != null ?
                    new ResponseEntity<>(new GenericDao<>(typeDto, null), HttpStatus.OK) :
                    new ResponseEntity<>(new GenericDao<>(null, List.of("Type with the name " + typeName + " was not found")), HttpStatus.NOT_FOUND);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    // To update type record
    @PutMapping
    public ResponseEntity<GenericDao<TypeDto>> editType(@RequestBody TypeDto typeDto) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {
            GenericDao<TypeDto> genericDao = typeService.editType(typeDto);

            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.OK) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



}