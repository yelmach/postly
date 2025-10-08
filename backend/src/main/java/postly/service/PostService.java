package postly.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import postly.dto.request.CreatePostRequest;
import postly.dto.request.UpdatePostRequest;
import postly.dto.response.PostMediaResponse;
import postly.dto.response.PostResponse;
import postly.dto.response.UserResponse;
import postly.entity.MediaType;
import postly.entity.PostEntity;
import postly.entity.PostMediaEntity;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.PostMediaRepository;
import postly.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        UserEntity currentUser = getCurrentUser();

        // 1. Create post entity
        PostEntity post = new PostEntity();
        post.setUser(currentUser);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        // 2. Upload files and create media entities
        List<String> mediaUrls = new ArrayList<>();
        List<PostMediaEntity> mediaEntities = new ArrayList<>();

        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (int i = 0; i < request.getFiles().size(); i++) {
                MultipartFile file = request.getFiles().get(i);

                String mediaUrl = fileStorageService.storePostMedia(file, post.getId());
                mediaUrls.add(mediaUrl);

                PostMediaEntity media = new PostMediaEntity();
                media.setPost(post);
                media.setMediaUrl(mediaUrl);
                media.setMediaType(determineMediaType(file));
                media.setDisplayOrder(i);
                mediaEntities.add(media);
            }

            postMediaRepository.saveAll(mediaEntities);
        }

        // 3. Replace placeholders with actual URLs
        String finalContent = replacePlaceholders(request.getContent(), mediaUrls);
        post.setContent(finalContent);
        post = postRepository.save(post);

        return mapToResponse(post, mediaEntities);
    }

    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        verifyOwnership(post);

        // 1. Get existing media
        List<PostMediaEntity> existingMedia = postMediaRepository.findByPostIdOrderByDisplayOrder(postId);

        // 2. Extract URLs from new content to determine what to keep
        Set<String> urlsInNewContent = extractMediaUrls(request.getContent());

        // 3. Delete media that's no longer in the content
        for (PostMediaEntity media : existingMedia) {
            if (!urlsInNewContent.contains(media.getMediaUrl())) {
                fileStorageService.deleteFile(media.getMediaUrl());
                postMediaRepository.delete(media);
            }
        }

        // 4. Upload new files
        List<String> newMediaUrls = new ArrayList<>();
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            int currentMaxOrder = existingMedia.stream()
                    .filter(m -> urlsInNewContent.contains(m.getMediaUrl()))
                    .mapToInt(PostMediaEntity::getDisplayOrder)
                    .max()
                    .orElse(-1);

            for (int i = 0; i < request.getFiles().size(); i++) {
                MultipartFile file = request.getFiles().get(i);
                String mediaUrl = fileStorageService.storePostMedia(file, postId);
                newMediaUrls.add(mediaUrl);

                PostMediaEntity media = new PostMediaEntity();
                media.setPost(post);
                media.setMediaUrl(mediaUrl);
                media.setMediaType(determineMediaType(file));
                media.setDisplayOrder(currentMaxOrder + i + 1);
                postMediaRepository.save(media);
            }
        }

        // 5. Replace placeholders in content
        String finalContent = replacePlaceholders(request.getContent(), newMediaUrls);

        // 6. Update post
        post.setTitle(request.getTitle());
        post.setContent(finalContent);
        post = postRepository.save(post);

        return mapToResponse(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        verifyOwnership(post);

        // Get all media and delete files
        List<PostMediaEntity> mediaFiles = postMediaRepository.findByPostIdOrderByDisplayOrder(postId);
        for (PostMediaEntity media : mediaFiles) {
            fileStorageService.deleteFile(media.getMediaUrl());
        }

        // Delete post (cascade will delete media records)
        postRepository.delete(post);
    }

    public PostResponse getPost(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        return mapToResponse(post);
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<PostEntity> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return posts.map(this::mapToResponse);
    }

    public Page<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        Page<PostEntity> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return posts.map(this::mapToResponse);
    }

    public List<PostResponse> getUserPostsList(Long userId) {
        List<PostEntity> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return posts.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Helper methods

    private UserEntity getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) auth.getPrincipal();
    }

    private void verifyOwnership(PostEntity post) {
        UserEntity currentUser = getCurrentUser();
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("You can only modify your own posts");
        }
    }

    private String replacePlaceholders(String content, List<String> mediaUrls) {
        String result = content;

        for (int i = 0; i < mediaUrls.size(); i++) {
            String placeholder = "PLACEHOLDER_" + i;
            String actualUrl = mediaUrls.get(i);
            result = result.replace(placeholder, actualUrl);
        }

        return result;
    }

    private Set<String> extractMediaUrls(String content) {
        Set<String> urls = new HashSet<>();

        // Regex to match markdown image/video syntax: ![alt](url)
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String url = matcher.group(1);
            // Only track our own uploaded media (starts with /uploads/posts/)
            if (url.startsWith("/uploads/posts/")) {
                urls.add(url);
            }
        }

        return urls;
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

        return MediaType.IMAGE; // Default fallback
    }

    private PostResponse mapToResponse(PostEntity post) {
        List<PostMediaEntity> mediaFiles = postMediaRepository.findByPostIdOrderByDisplayOrder(post.getId());
        return mapToResponse(post, mediaFiles);
    }

    private PostResponse mapToResponse(PostEntity post, List<PostMediaEntity> mediaFiles) {
        UserResponse author = UserResponse.fromUser(post.getUser()).build();

        List<PostMediaResponse> mediaResponses = mediaFiles.stream()
                .map(media -> new PostMediaResponse(
                        media.getId(),
                        media.getMediaUrl(),
                        media.getMediaType(),
                        media.getDisplayOrder(),
                        media.getCreatedAt()))
                .collect(Collectors.toList());

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                author,
                mediaResponses,
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
