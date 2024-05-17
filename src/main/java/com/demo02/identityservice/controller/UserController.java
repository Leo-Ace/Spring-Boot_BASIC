package com.demo02.identityservice.controller;

import com.demo02.identityservice.dto.request.ApiResponse;
import com.demo02.identityservice.dto.request.UserCreationRequest;
import com.demo02.identityservice.dto.request.UserUpdateRequest;
import com.demo02.identityservice.entity.User;
import com.demo02.identityservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> userApiResponse = new ApiResponse<User>();

        userApiResponse.setResult(userService.createUser(request));

        return userApiResponse;
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        return "User has been deleted!";
    }
}

