package com.example.milk_order_app.controller;

import com.example.milk_order_app.dto.*;
import com.example.milk_order_app.entity.AppUser;
import com.example.milk_order_app.jwt.JwtService;
import com.example.milk_order_app.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Transactional
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ==========================
    // ✅ SIGNUP
    // ==========================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {

        if (repository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().trim().toUpperCase());
        user.setAddress(request.getAddress());

        AppUser savedUser = repository.save(user);

        return ResponseEntity.ok(
                new SignupResponse(
                        savedUser.getId(),
                        savedUser.getUsername(),
                        savedUser.getEmail(),
                        savedUser.getMobile(),
                        savedUser.getRole()
                )
        );
    }

    // ==========================
    // ✅ LOGIN (USERNAME / EMAIL / MOBILE)
    // ==========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        if (request.getLoginId() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body("LoginId and Password required");
        }

        // 🔥 Find user using OR query
        AppUser user = repository
                .findByUsernameOrEmailOrMobile(
                        request.getLoginId(),
                        request.getLoginId(),
                        request.getLoginId()
                )
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        try {
            // 🔥 Authenticate using real username
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body("Invalid Credentials");
        }

        // 🔥 Generate Tokens
        String accessToken =
                jwtService.generateAccessToken(
                        user.getUsername(),
                        user.getRole()
                );

        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getUsername()
                );

        user.setRefreshToken(refreshToken);
        repository.save(user);

        return ResponseEntity.ok(
                new AuthResponse(
                        accessToken,
                        refreshToken,
                        user.getUsername(),
                        user.getRole()
                )
        );
    }

    // ==========================
    // ✅ REFRESH TOKEN
    // ==========================
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        String username = jwtService.extractUsername(refreshToken);

        AppUser user = repository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!refreshToken.equals(user.getRefreshToken())
                || jwtService.isTokenExpired(refreshToken)) {

            return ResponseEntity.status(401)
                    .body("Invalid or Expired Refresh Token");
        }

        String newAccessToken =
                jwtService.generateAccessToken(
                        username,
                        user.getRole()
                );

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }
}