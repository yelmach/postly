package postly.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import postly.exception.ApiException;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = { "jpg", "jpeg", "png", "webp" };

    public String storeProfilePicture(MultipartFile file, Long userId) {
        validateFile(file);

        try {
            Path uploadPath = Paths.get(uploadDir, "profiles");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileExtension = getFileExtension(file);
            String newFilename = "user_" + userId + "_" + UUID.randomUUID().toString() + "." + fileExtension;

            Path targetLocation = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/profiles/" + newFilename;

        } catch (IOException ex) {
            throw ApiException.internalError("Failed to store file: " + ex.getMessage());
        }
    }

    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }

        try {
            Path path = Paths.get(uploadDir).resolve(filePath.replace("/uploads/", "")).normalize();
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            System.err.println("Failed to delete file: " + filePath);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw ApiException.badRequest("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw ApiException.badRequest("File size exceeds maximum limit of 5MB");
        }

        String extension = getFileExtension(file);

        boolean isAllowed = false;
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equalsIgnoreCase(extension)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw ApiException.badRequest("Only JPG, JPEG, PNG, and WebP images are allowed");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw ApiException.badRequest("Invalid file type. Only images are allowed");
        }
    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw ApiException.badRequest("File must have a name");
        }

        String filename = StringUtils.cleanPath(originalFilename);

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw ApiException.badRequest("File must have an extension");
        }
        return filename.substring(lastDotIndex + 1);
    }
}