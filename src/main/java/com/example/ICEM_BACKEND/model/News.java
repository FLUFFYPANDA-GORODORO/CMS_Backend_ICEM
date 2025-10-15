package com.example.ICEM_BACKEND.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String pdfUrl;     // ✅ instead of image URL

    private String publicId;   // ✅ Cloudinary reference

    private String author;

    private String date;       // Optional (string or LocalDateTime)
}
