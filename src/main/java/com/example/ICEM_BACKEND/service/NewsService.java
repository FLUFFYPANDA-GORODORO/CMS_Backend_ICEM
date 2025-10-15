package com.example.ICEM_BACKEND.service;

import com.example.ICEM_BACKEND.model.News;
import com.example.ICEM_BACKEND.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // GET all news
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    // GET by ID
    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    // POST - Create news with PDF
    public News createNews(String title, String description, String author, MultipartFile pdfFile) throws IOException {
        News news = new News();
        news.setTitle(title);
        news.setDescription(description);
        news.setAuthor(author);
        news.setDate(LocalDate.now().toString());

        // ✅ Upload PDF if provided
        if (pdfFile != null && !pdfFile.isEmpty()) {
            Map<String, String> uploadResult = cloudinaryService.uploadPdf(pdfFile);
            news.setPdfUrl(uploadResult.get("url"));
            news.setPublicId(uploadResult.get("publicId"));
        }

        return newsRepository.save(news);
    }

    // PUT - Update news
    public News updateNews(Long id, String title, String description, String author, MultipartFile pdfFile) throws IOException {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));

        existingNews.setTitle(title);
        existingNews.setDescription(description);
        existingNews.setAuthor(author);

        // ✅ Update PDF if provided
        if (pdfFile != null && !pdfFile.isEmpty()) {
            if (existingNews.getPublicId() != null) {
                cloudinaryService.deleteFile(existingNews.getPublicId());
            }

            Map<String, String> uploadResult = cloudinaryService.uploadPdf(pdfFile);
            existingNews.setPdfUrl(uploadResult.get("url"));
            existingNews.setPublicId(uploadResult.get("publicId"));
        }

        return newsRepository.save(existingNews);
    }

    // DELETE - Delete news and PDF from Cloudinary
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));

        try {
            if (news.getPublicId() != null) {
                cloudinaryService.deleteFile(news.getPublicId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting PDF from Cloudinary", e);
        }

        newsRepository.deleteById(id);
    }
}
