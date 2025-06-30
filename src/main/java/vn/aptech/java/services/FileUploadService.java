package vn.aptech.java.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    @Value("${file.upload-dir:src/main/resources/static/images}")
    private String uploadDir;

    public String saveFile(MultipartFile file, String subfolder) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir, subfolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate shorter unique filename for database compatibility (255 chars limit)
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Create shorter filename using timestamp + random string (much shorter than
        // UUID)
        long timestamp = System.currentTimeMillis();
        int randomNum = (int) (Math.random() * 9000) + 1000; // 4 digit random number
        String uniqueFilename = timestamp + "_" + randomNum + fileExtension;

        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return relative path for web access (ensure it's under 255 chars)
        String relativePath = "/images/" + subfolder + "/" + uniqueFilename;

        // Additional safety check for path length
        if (relativePath.length() > 255) {
            throw new IOException("Generated file path is too long for database storage");
        }

        return relativePath;
    }

    public boolean deleteFile(String relativePath) {
        try {
            if (relativePath != null && relativePath.startsWith("/images/")) {
                Path filePath = Paths.get(uploadDir, relativePath.substring("/images/".length()));
                return Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isValidImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/bmp") ||
                contentType.equals("image/webp"));
    }

    public boolean isValidImageUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        // Check URL length for database compatibility
        if (url.length() > 255) {
            return false;
        }

        // Basic URL validation for images
        String lowerUrl = url.toLowerCase();
        return lowerUrl.matches("https?://.*\\.(jpg|jpeg|png|gif|bmp|webp).*") ||
                lowerUrl.contains("image") ||
                lowerUrl.startsWith("/images/") ||
                lowerUrl.startsWith("/assets/images/");
    }

    public String validateAndTruncateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }

        // Truncate URL if it's too long for database
        if (url.length() > 255) {
            return url.substring(0, 252) + "...";
        }

        return url;
    }
}
