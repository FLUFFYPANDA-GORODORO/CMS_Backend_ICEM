package com.example.ICEM_BACKEND.controller;

import com.example.ICEM_BACKEND.model.Admin;
import com.example.ICEM_BACKEND.service.AdminService;
import com.example.ICEM_BACKEND.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    // ✅ REGISTER new admin
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String email, @RequestParam String password) {
        if (adminService.getByEmail(email) != null) {
            return ResponseEntity.status(400).body("Admin already exists with this email");
        }

        Admin newAdmin = adminService.createAdmin(email, password);
        return ResponseEntity.ok(java.util.Map.of(
                "message", "Admin registered successfully",
                "email", newAdmin.getEmail(),
                "id", newAdmin.getId()
        ));
    }

    // ✅ LOGIN existing admin
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Admin admin = adminService.getByEmail(email);

        if (admin == null || !adminService.checkPassword(admin, password)) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        String token = jwtService.generateToken(email);
        return ResponseEntity.ok(java.util.Map.of(
                "token", token,
                "email", email
        ));
    }

    // ✅ GET CURRENT ADMIN INFO (validate token)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentAdmin(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(400).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);

            if (!jwtService.isTokenValid(token, email)) {
                return ResponseEntity.status(401).body("Invalid or expired token");
            }

            Admin admin = adminService.getByEmail(email);
            if (admin == null) {
                return ResponseEntity.status(404).body("Admin not found");
            }

            return ResponseEntity.ok(java.util.Map.of(
                    "email", admin.getEmail(),
                    "id", admin.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }
}
