package postly.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import postly.dto.response.PostMediaResponse;
import postly.dto.response.UserResponse;
import postly.entity.MediaType;
import postly.entity.PostMediaEntity;
import postly.entity.UserEntity;
import postly.repository.PostMediaRepository;
import postly.repository.PostRepository;
import postly.repository.SubscriptionRepository;
import postly.repository.UserRepository;

@Service
public class MediaUploadService {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

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

    public UserResponse updateProfilePicture(MultipartFile file) {
        UserEntity user = userService.getCurrentUserEntity();

        if (user.getProfileUrl() != null && !user.getProfileUrl().isEmpty()) {
            fileStorageService.deleteFile(user.getProfileUrl());
        }

        String profileUrl = fileStorageService.storeProfilePicture(file, user.getId());
        user.setProfileUrl(profileUrl);

        UserEntity updatedUser = userRepository.save(user);

        return buildUserResponse(updatedUser);
    }

    public UserResponse removeProfilePicture() {
        UserEntity user = userService.getCurrentUserEntity();

        if (user.getProfileUrl() != null && !user.getProfileUrl().isEmpty()) {
            fileStorageService.deleteFile(user.getProfileUrl());
        }

        user.setProfileUrl(null);

        UserEntity updatedUser = userRepository.save(user);

        return buildUserResponse(updatedUser);
    }

    private UserResponse buildUserResponse(UserEntity user) {
        long postsCount = postRepository.countByUserId(user.getId());
        long subscribersCount = subscriptionRepository.countBySubscribedToId(user.getId());
        long subscribedCount = subscriptionRepository.countBySubscriberId(user.getId());

        return UserResponse.fromUser(user)
                .postsCount(postsCount)
                .subscribersCount(subscribersCount)
                .subscribedCount(subscribedCount)
                .build();
    }
}
