package com.example.usermanagement.controller;

import com.example.usermanagement.dto.ForgotPasswordRequest;
import com.example.usermanagement.dto.LoginRequest;
import com.example.usermanagement.dto.ResetPasswordRequest;
import com.example.usermanagement.dto.UpdateProfileRequest;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getProfile(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean authenticated = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (authenticated) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password!");
        }
    }

    // Forgot Password (Request OTP)
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String response = userService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    // Reset Password Using OTP
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String response = userService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    // Update Profile
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest request) {
        String response = userService.updateProfile(request);
        return ResponseEntity.ok(response);
    }

    // Get Profile by Username
    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getProfileByUsername(@PathVariable String username) {
        User user = userService.getProfileByUsername(username);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found!");
        }
        return ResponseEntity.ok(user);
    }

}

