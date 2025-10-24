package com.example.ICEM_BACKEND.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email; // ✅ Changed from username to email

    @Column(nullable = false)
    private String password; // ✅ BCrypt encrypted
}
