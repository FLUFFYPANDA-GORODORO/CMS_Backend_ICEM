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

    private String pdfUrl;     // ✅ Full Cloudinary URL
    private String publicId;   // ✅ Cloudinary public reference
    private String author;
    private String date;       // ✅ Stored as String for flexibility
}
