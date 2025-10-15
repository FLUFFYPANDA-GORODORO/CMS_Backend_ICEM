package com.example.ICEM_BACKEND.service;

import com.example.ICEM_BACKEND.model.Banner;
import com.example.ICEM_BACKEND.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // GET all banners
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    // GET banner by ID
    public Optional<Banner> getBannerById(Long id) {
        return bannerRepository.findById(id);
    }

    // POST - Create new banner (upload new image)
    public Banner uploadBanner(MultipartFile image) throws IOException {
        Map<String, String> uploadResult = cloudinaryService.uploadFile(image);

        Banner banner = new Banner();
        banner.setImageUrl(uploadResult.get("url"));
        banner.setPublicId(uploadResult.get("publicId"));

        return bannerRepository.save(banner);
    }

    // PUT - Update existing banner image
    public Banner updateBanner(Long id, MultipartFile newImage) throws IOException {
        Banner existingBanner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));

        // Delete old image from Cloudinary
        if (existingBanner.getPublicId() != null) {
            cloudinaryService.deleteFile(existingBanner.getPublicId());
        }

        // Upload new image
        Map<String, String> uploadResult = cloudinaryService.uploadFile(newImage);
        existingBanner.setImageUrl(uploadResult.get("url"));
        existingBanner.setPublicId(uploadResult.get("publicId"));

        return bannerRepository.save(existingBanner);
    }

    // DELETE - Remove banner and delete image from Cloudinary
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));

        try {
            if (banner.getPublicId() != null) {
                cloudinaryService.deleteFile(banner.getPublicId());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting image from Cloudinary", e);
        }

        bannerRepository.deleteById(id);
    }
}
