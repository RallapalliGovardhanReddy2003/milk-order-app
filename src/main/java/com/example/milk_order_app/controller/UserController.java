package com.example.milk_order_app.controller;

import com.example.milk_order_app.dto.UserResponse;
import com.example.milk_order_app.entity.AppUser;
import com.example.milk_order_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
@Transactional
public class UserController {

    @Autowired
    private UserRepository repository;

    // ==========================================
    // ✅ USER MANAGEMENT APIs (Admin)
    // ==========================================

    // ✅ GET ALL USERS
    @GetMapping("/getallusers")
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        return repository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getMobile(),
                        user.getRole(),
                        user.getAddress()
                ))
                .collect(Collectors.toList());
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        log.info("Fetching user with ID: " + id);

        AppUser user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMobile(),
                user.getRole(),
                user.getAddress()
        );
    }

    // ✅ UPDATE USER BY ID (Admin)
    @PutMapping("/updating/{id}")

    public UserResponse updateUser(@PathVariable Long id,
                                   @RequestBody AppUser updatedUser) {
        log.info("Received user update request for ID: " + id + " with data: " + updatedUser);

        AppUser user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setMobile(updatedUser.getMobile());
        user.setRole(updatedUser.getRole());
        user.setAddress(updatedUser.getAddress());
        log.info("Updating user with ID: " + id);

        repository.save(user);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMobile(),
                user.getRole(),
                user.getAddress()
        );
    }

    // ✅ DELETE USER
    @DeleteMapping("/deleting/{id}")
    public String deleteUser(@PathVariable Long id) {
        log.info("Received user deletion request for ID: " + id);

        repository.deleteById(id);
        return "User deleted successfully";
    }

    // ==========================================
    // ✅ USER PROFILE APIs (Logged-in User)
    // ==========================================

    // ✅ GET MY PROFILE
    @GetMapping("/profile")
    public UserResponse getMyProfile(Authentication authentication) {
        log.info("Fetching profile for user: " + authentication.getName());

        String username = authentication.getName();

        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Found user: " + user.getUsername() + " with email: " + user.getEmail());
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMobile(),
                user.getRole(),
                user.getAddress()
        );
    }

    // ✅ UPDATE MY PROFILE
    @PutMapping("/profile")
    public UserResponse updateMyProfile(Authentication authentication,
                                        @RequestBody AppUser updatedUser) {
        log.info("Received profile update request for user: " + authentication.getName() + " with data: " + updatedUser);

        String username = authentication.getName();

        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(updatedUser.getEmail());
        user.setMobile(updatedUser.getMobile());
        log.info("Updating profile for user: " + username + " to new email: " + updatedUser.getEmail() + " and mobile: " + updatedUser.getMobile());

        repository.save(user);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMobile(),
                user.getRole(),
                user.getAddress()
        );
    }
}
