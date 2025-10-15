package com.example.ICEM_BACKEND.controller;

import com.example.ICEM_BACKEND.model.Banner;
import com.example.ICEM_BACKEND.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin(origins = "http://localhost:5173")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    // GET all banners
    @GetMapping
    public List<Banner> getAllBanners() {
        return bannerService.getAllBanners();
    }

    // GET banner by ID
    @GetMapping("/{id}")
    public ResponseEntity<Banner> getBannerById(@PathVariable Long id) {
        Optional<Banner> banner = bannerService.getBannerById(id);
        return banner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - Upload new banner
    @PostMapping("/upload")
    public ResponseEntity<Banner> uploadBanner(@RequestParam("image") MultipartFile image) throws IOException {
        Banner banner = bannerService.uploadBanner(image);
        return ResponseEntity.ok(banner);
    }

    // PUT - Update existing banner image
    @PutMapping("/{id}")
    public ResponseEntity<Banner> updateBanner(@PathVariable Long id, @RequestParam("image") MultipartFile newImage) throws IOException {
        Banner updatedBanner = bannerService.updateBanner(id, newImage);
        return ResponseEntity.ok(updatedBanner);
    }

    // DELETE - Delete banner
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return ResponseEntity.noContent().build();
    }
}
