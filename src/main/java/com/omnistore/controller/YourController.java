package com.omnistore.controller;

import com.omnistore.entity.User;
import com.omnistore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class YourController {

    private final UserService userService;

    public YourController(UserService userService) {
        this.userService = userService;
    }

    // Ensure the response type is set to JSON
    @GetMapping("/user")
    public ResponseEntity<User> getUser() {
        User user = userService.getUserDetails();
        return ResponseEntity.ok(user);  // Ensures a valid JSON response
    }
}
