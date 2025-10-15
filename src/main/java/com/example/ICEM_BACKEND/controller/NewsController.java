package com.example.ICEM_BACKEND.controller;

import com.example.ICEM_BACKEND.model.News;
import com.example.ICEM_BACKEND.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:5173")
public class NewsController {

    @Autowired
    private NewsService newsService;

    // GET all
    @GetMapping
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.getNewsById(id);
        return news.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - Create new news
    @PostMapping("/upload")
    public ResponseEntity<News> createNews(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam(value = "pdf", required = false) MultipartFile pdfFile
    ) throws IOException {
        News news = newsService.createNews(title, description, author, pdfFile);
        return ResponseEntity.ok(news);
    }

    // PUT - Update existing news
    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam(value = "pdf", required = false) MultipartFile pdfFile
    ) throws IOException {
        News updatedNews = newsService.updateNews(id, title, description, author, pdfFile);
        return ResponseEntity.ok(updatedNews);
    }

    // DELETE - Delete news
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}
