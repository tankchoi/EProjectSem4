package vn.aptech.java.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class ImgUploadUtil {
    private static final String UPLOAD_DIR = "uploads";
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp");

    public static String saveFile(MultipartFile file, String subfolder) throws IOException {
        return saveFile(file, UPLOAD_DIR, subfolder);
    }

    public static String saveFile(MultipartFile file, String uploadDir, String subfolder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Tệp không được để trống");
        }

        // Kiểm tra định dạng ảnh hợp lệ
        if (!isValidImageFormat(file)) {
            throw new IllegalArgumentException("Định dạng ảnh không hợp lệ. Chỉ cho phép: " +
                    String.join(", ", ALLOWED_EXTENSIONS));
        }

        // Tạo đường dẫn đầy đủ, bao gồm thư mục con nếu có
        Path uploadPath = Paths.get(uploadDir);
        if (subfolder != null && !subfolder.trim().isEmpty()) {
            uploadPath = uploadPath.resolve(subfolder.trim());
        }

        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên tệp ngẫu nhiên để tránh trùng lặp
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "." + extension;

        // Lưu tệp
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        // Trả về đường dẫn tương đối bao gồm thư mục uploads/
        return "/" + Paths
                .get(UPLOAD_DIR,
                        subfolder != null && !subfolder.trim().isEmpty() ? Paths.get(subfolder, newFilename).toString()
                                : newFilename)
                .toString()
                .replace("\\", "/");
    }

    public static boolean deleteFile(String filename) {
        return deleteFile(filename, UPLOAD_DIR);
    }

    public static boolean deleteFile(String filename, String directory) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        if (filename.startsWith("/")) {
            filename = filename.substring(1);
        }
        Path filePath;
        if (filename.startsWith(directory)) {
            filePath = Paths.get(filename);
        } else {
            filePath = Paths.get(directory, filename);
        }
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidImageFormat(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            return false;
        }

        String extension = getFileExtension(filename).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    private static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty() || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
