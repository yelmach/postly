package postly.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import postly.dto.response.PostMediaResponse;
import postly.entity.MediaType;
import postly.entity.PostMediaEntity;
import postly.repository.PostMediaRepository;

@Service
public class MediaUploadService {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PostMediaRepository postMediaRepository;

    public PostMediaResponse saveMedia(MultipartFile file) {
        String mediaUrl = fileStorageService.storePostMedia(file);
        MediaType mediaType = determineMediaType(file);

        PostMediaEntity media = new PostMediaEntity();
        media.setMediaUrl(mediaUrl);
        media.setMediaType(mediaType);
        media.setIsTemporary(true);
        media.setExpiresAt(LocalDateTime.now().plusHours(24));

        media = postMediaRepository.save(media);

        return new PostMediaResponse(media.getId(), media.getMediaUrl(), media.getMediaType());
    }

    private MediaType determineMediaType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return MediaType.IMAGE;
            } else if (contentType.startsWith("video/")) {
                return MediaType.VIDEO;
            }
        }

        return MediaType.IMAGE;
    }
}
