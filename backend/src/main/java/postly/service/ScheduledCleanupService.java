package postly.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import postly.entity.PostMediaEntity;
import postly.repository.PostMediaRepository;

@Service
public class ScheduledCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledCleanupService.class);

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Scheduled(cron = "0 0 2 * * *") // Runs daily at 2 AM
    @Transactional
    public void cleanupExpiredTemporaryMedia() {
        logger.info("Starting cleanup of expired temporary media");

        LocalDateTime now = LocalDateTime.now();
        List<PostMediaEntity> expiredMedia = postMediaRepository.findByIsTemporaryTrueAndExpiresAtBefore(now);

        int successCount = 0;
        int failureCount = 0;

        for (PostMediaEntity media : expiredMedia) {
            try {
                fileStorageService.deleteFile(media.getMediaUrl());

                postMediaRepository.delete(media);

                successCount++;
                logger.debug("Deleted expired temporary media: {}", media.getMediaUrl());
            } catch (Exception e) {
                failureCount++;
                logger.error("Failed to delete expired temporary media: {}", media.getMediaUrl(), e);
            }
        }

        logger.info("Cleanup completed. Deleted: {}, Failed: {}, Total: {}", successCount, failureCount,
                expiredMedia.size());
    }
}
