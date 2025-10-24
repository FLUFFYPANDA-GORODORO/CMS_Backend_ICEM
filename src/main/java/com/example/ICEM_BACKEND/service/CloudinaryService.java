package com.example.ICEM_BACKEND.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    // ✅ Upload PDF file
    public Map<String, String> uploadPdf(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String baseName = (originalFileName != null)
                ? originalFileName.replace(".pdf", "")
                : "file_" + System.currentTimeMillis();

        // ✅ Upload PDF to Cloudinary (Unsigned preset)
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "raw",            // Required for PDFs
                        "upload_preset", "public_upload",  // Unsigned preset name
                        "folder", "news_pdfs",             // Folder in Cloudinary
                        "public_id", baseName,             // Clean file name
                        "use_filename", true,
                        "unique_filename", true
                )
        );

        // ✅ Extract details
        String pdfUrl = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();

        System.out.println("✅ Uploaded PDF:");
        System.out.println("URL: " + pdfUrl);
        System.out.println("Public ID: " + publicId);
        System.out.println("Resource type: " + uploadResult.get("resource_type"));

        return Map.of(
                "url", pdfUrl,
                "publicId", publicId
        );
    }

    // ✅ Delete PDF from Cloudinary
    public void deletePdf(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));
    }

    // ✅ Generic image/file upload (optional utility)
    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));
        return Map.of(
                "url", uploadResult.get("secure_url").toString(),
                "publicId", uploadResult.get("public_id").toString()
        );
    }

    // ✅ Delete any file (generic)
    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
