package com.example.projectApp.controller;
import com.example.projectApp.Dao.GenericDao;
import com.example.projectApp.Dto.UserDto;
import com.example.projectApp.model.LoginRequest;
import com.example.projectApp.model.LoginResponse;
import com.example.projectApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/users")

public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }
// User register
    @PostMapping("/register")
    public ResponseEntity<GenericDao<UserDto>> createUser(@RequestBody UserDto user){
        try {
            GenericDao<UserDto> genericDao = userService.createUser(user);
            return genericDao.getErrors().isEmpty() ?
                    new ResponseEntity<>(genericDao, HttpStatus.CREATED) :
                    new ResponseEntity<>(genericDao, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Exception caught and log saved."+e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
// User login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            return userService.loginUser(loginRequest);
        } catch (Exception e) {
            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@Param("code") String code) {
        try {
            if (userService.verify(code)) {
                return ResponseEntity.ok("User verified Successfully!");
            } else {
                return ResponseEntity.status(403).body(new LoginResponse("Code Verification is wrong. please provide the correct one."));
            }
        } catch (Exception e) {
            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
// request to reset password
    @PostMapping("/request-password-reset")

    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        try {
            boolean result = userService.requestPasswordReset(email);
            if (result) {
                return ResponseEntity.ok("Password reset code sent.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this email does not exist.");
            }
        } catch (Exception e) {

            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")

    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            boolean result = userService.resetPassword(token, newPassword);
            if (result) {
                return ResponseEntity.ok("Password has been successfully reset.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
            }
        } catch (Exception e) {
            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change-password")

    public ResponseEntity<?> changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        try {
            boolean result = userService.changePassword(oldPassword, newPassword);
            if (result) {
                return ResponseEntity.ok("Password has been successfully changed.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
            }
        } catch (Exception e) {
            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/deactivate")

    public ResponseEntity<?> deactivateUser(@RequestParam Long uid) {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {
                GenericDao<Boolean> result = userService.deleteUser(uid);
                if (result.getObject()) {
                    return ResponseEntity.ok("User has been deactivated successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getErrors());
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {

            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<GenericDao<List<UserDto>>> getAllUsers() {
        try {
            if (UserService.getCurrentLoggedInUser().getRole().getName().equalsIgnoreCase("Manager")) {
                GenericDao<List<UserDto>> genericDao = new GenericDao<>();
                genericDao.setObject(userService.getAll(false));
                return genericDao.getErrors().isEmpty() ?
                        new ResponseEntity<>(genericDao, HttpStatus.OK) :
                        new ResponseEntity<>(genericDao, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {

            System.out.println("Exception caught and log saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}