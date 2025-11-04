package postly.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import postly.exception.ApiException;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final long MAX_PROFILE_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50MB
    private static final String[] ALLOWED_IMAGE_EXTENSIONS = { "jpg", "jpeg", "png", "webp", "gif" };
    private static final String[] ALLOWED_VIDEO_EXTENSIONS = { "mp4", "webm", "mov" };

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

    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        try {
            Path path = Paths.get(uploadDir).resolve(filePath.replace("/uploads/", "")).normalize();
            boolean deleted = Files.deleteIfExists(path);
            if (!deleted) {
                logger.warn("File does not exist or could not be deleted: {}", filePath);
            }
            return deleted;
        } catch (IOException ex) {
            logger.error("Failed to delete file: {} - {}", filePath, ex.getMessage(), ex);
            return false;
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw ApiException.badRequest("File is empty");
        }

        if (file.getSize() > MAX_PROFILE_FILE_SIZE) {
            throw ApiException.badRequest("File size exceeds maximum limit of 5MB");
        }

        String extension = getFileExtension(file);

        boolean isAllowed = false;
        for (String allowedExt : ALLOWED_IMAGE_EXTENSIONS) {
            if (allowedExt.equalsIgnoreCase(extension)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw ApiException.badRequest("Only JPG, JPEG, PNG, WebP, and GIF images are allowed");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw ApiException.badRequest("Invalid file type. Only images are allowed");
        }
    }

    public String storePostMedia(MultipartFile file) {
        validatePostMedia(file);

        try {
            Path uploadPath = Paths.get(uploadDir, "posts");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileExtension = getFileExtension(file);
            String newFilename = "media_" + UUID.randomUUID().toString() + "." + fileExtension;

            Path targetLocation = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/posts/" + newFilename;

        } catch (IOException ex) {
            throw ApiException.internalError("Failed to store file: " + ex.getMessage());
        }
    }

    private void validatePostMedia(MultipartFile file) {
        if (file.isEmpty()) {
            throw ApiException.badRequest("File is empty");
        }

        String extension = getFileExtension(file);
        String contentType = file.getContentType();

        boolean isImage = isImageExtension(extension);
        boolean isVideo = isVideoExtension(extension);

        if (!isImage && !isVideo) {
            throw ApiException.badRequest(
                    "Only JPG, JPEG, PNG, WebP, GIF images and MP4, WebM, MOV videos are allowed");
        }

        if (isImage) {
            if (file.getSize() > MAX_IMAGE_SIZE) {
                throw ApiException.badRequest("Image size exceeds maximum limit of 10MB");
            }
            if (contentType == null || !contentType.startsWith("image/")) {
                throw ApiException.badRequest("Invalid file type. Only images are allowed for this extension");
            }
        } else if (isVideo) {
            if (file.getSize() > MAX_VIDEO_SIZE) {
                throw ApiException.badRequest("Video size exceeds maximum limit of 50MB");
            }
            if (contentType == null || !contentType.startsWith("video/")) {
                throw ApiException.badRequest("Invalid file type. Only videos are allowed for this extension");
            }
        }
    }

    private boolean isImageExtension(String extension) {
        for (String allowed : ALLOWED_IMAGE_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVideoExtension(String extension) {
        for (String allowed : ALLOWED_VIDEO_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
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